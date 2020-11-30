package com.rbkmoney.threeds.server.converter.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.account.AccountInfo;
import com.rbkmoney.threeds.server.domain.account.AccountInfoWrapper;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptions;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptionsWrapper;
import com.rbkmoney.threeds.server.domain.merchant.MerchantRiskIndicator;
import com.rbkmoney.threeds.server.domain.merchant.MerchantRiskIndicatorWrapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.utils.CrutchChecker.isMirCrutchCondition;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestToAReqConverter implements Converter<ValidationResult, Message> {

    /**
     * Seq 3.82 [Req 346] For a Decoupled Authentication transaction, at a minimum wait the
     * specified 3DS Requestor Decoupled Max Time plus 30 seconds for the RReq
     * message. If an RReq message is never received, further processing is outside
     * the scope of 3- Secure processing.
     */
    private static final int MAX_GRACE_PERIOD = 30;

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;
    private final IdGenerator idGenerator;

    @Override
    public Message convert(ValidationResult validationResult) {
        RBKMoneyAuthenticationRequest request = (RBKMoneyAuthenticationRequest) validationResult.getMessage();

        AReq aReq = AReq.builder()
                .threeDSCompInd(getValue(request.getThreeDSCompInd()))
                .threeDSRequestorAuthenticationInd(getValue(request.getThreeDSRequestorAuthenticationInd()))
                .threeDSRequestorAuthenticationInfo(getThreeDSRequestorAuthenticationInfo(request))
//              .threeDSReqAuthMethodInd(null) todo null? emvco: -
                .threeDSRequestorChallengeInd(getValue(request.getThreeDSRequestorChallengeInd()))
                .threeDSRequestorDecMaxTime(request.getThreeDSRequestorDecMaxTime())
                .threeDSRequestorDecReqInd(getValue(request.getThreeDSRequestorDecReqInd()))
                .threeDSRequestorID(request.getThreeDSRequestorID())
                .threeDSRequestorName(request.getThreeDSRequestorName())
                .threeDSRequestorPriorAuthenticationInfo(getThreeDSRequestorPriorAuthenticationInfo(request))
                .threeDSRequestorURL(request.getThreeDSRequestorURL())
                .threeDSServerRefNumber(rbkMoneyDsProviderHolder.getEnvironmentProperties().getThreeDsServerRefNumber())
                .threeDSServerOperatorID(request.getThreeDSServerOperatorID())
                .threeDSServerTransID(idGenerator.generateUUID())
                .threeDSServerURL(getThreeDsServerUrl(request))
                .threeRIInd(getValue(request.getThreeRIInd()))
                .acctType(getValue(request.getAcctType()))
                .acquirerBIN(request.getAcquirerBIN())
                .acquirerMerchantID(request.getAcquirerMerchantID())
                .addrMatch(getValue(request.getAddrMatch()))
                .broadInfo(request.getBroadInfo())
                .browserAcceptHeader(request.getBrowserAcceptHeader())
                .browserIP(request.getBrowserIP())
                .browserJavaEnabled(request.getBrowserJavaEnabled())
                .browserJavascriptEnabled(request.getBrowserJavascriptEnabled())
                .browserLanguage(request.getBrowserLanguage())
                .browserColorDepth(getValue(request.getBrowserColorDepth()))
                .browserScreenHeight(request.getBrowserScreenHeight())
                .browserScreenWidth(request.getBrowserScreenWidth())
                .browserTZ(request.getBrowserTZ())
                .browserUserAgent(request.getBrowserUserAgent())
                .cardExpiryDate(request.getCardExpiryDate())
                .acctInfo(getAcctInfo(request))
                .acctNumber(request.getAcctNumber())
                .acctID(request.getAcctID())
                .billingAddress(
                        Address.builder()
                                .addrCity(request.getBillingAddress().getAddrCity())
                                .addrCountry(request.getBillingAddress().getAddrCountry())
                                .addrLine1(request.getBillingAddress().getAddrLine1())
                                .addrLine2(request.getBillingAddress().getAddrLine2())
                                .addrLine3(request.getBillingAddress().getAddrLine3())
                                .addrPostCode(request.getBillingAddress().getAddrPostCode())
                                .addrState(request.getBillingAddress().getAddrState())
                                .build()
                )
                .email(request.getEmail())
                .homePhone(request.getHomePhone())
                .mobilePhone(request.getMobilePhone())
                .cardholderName(request.getCardholderName())
                .shippingAddress(
                        Address.builder()
                                .addrCity(request.getShippingAddress().getAddrCity())
                                .addrCountry(request.getShippingAddress().getAddrCountry())
                                .addrLine1(request.getShippingAddress().getAddrLine1())
                                .addrLine2(request.getShippingAddress().getAddrLine2())
                                .addrLine3(request.getShippingAddress().getAddrLine3())
                                .addrPostCode(request.getShippingAddress().getAddrPostCode())
                                .addrState(request.getShippingAddress().getAddrState())
                                .build()
                )
                .workPhone(request.getWorkPhone())
                .deviceChannel(getValue(request.getDeviceChannel()))
//              .deviceInfo(null) emvco: source ds
                .deviceRenderOptions(getDeviceRenderOptions(request))
//              .dsReferenceNumber(null) emvco: source ds
//              .dsTransID(null) emvco: source ds
//              .dsURL(null) emvco: source ds
                .payTokenInd(request.getPayTokenInd())
                .payTokenSource(getValue(request.getPayTokenSource()))
                .purchaseInstalData(getPurchaseInstalData(request))
                .mcc(request.getMcc())
                .merchantCountryCode(request.getMerchantCountryCode())
                .merchantName(request.getMerchantName())
                .merchantRiskIndicator(getMerchantRiskIndicator(request))
                .messageCategory(getValue(request.getMessageCategory()))
                .messageExtension(getValue(request.getMessageExtension()))
                .notificationURL(request.getNotificationURL())
                .purchaseAmount(request.getPurchaseAmount())
                .purchaseCurrency(request.getPurchaseCurrency())
                .purchaseExponent(request.getPurchaseExponent())
                .purchaseDate(getValue(request.getPurchaseDate()))
                .recurringExpiry(getValue(request.getRecurringExpiry()))
                .recurringFrequency(request.getRecurringFrequency())
                .sdkAppID(request.getSdkAppID())
                .sdkEncData(request.getSdkEncData())
                .sdkEphemPubKey(request.getSdkEphemPubKey())
                .sdkMaxTimeout(request.getSdkMaxTimeout())
                .sdkReferenceNumber(request.getSdkReferenceNumber())
                .sdkTransID(request.getSdkTransID())
                .transType(getValue(request.getTransType()))
                .whiteListStatus(getValue(request.getWhiteListStatus()))
                .whiteListStatusSource(getValue(request.getWhiteListStatusSource()))
                .decoupledAuthMaxTime(getDecAuthMaxTime(request))
                .build();
        aReq.setMessageVersion(request.getMessageVersion());
        return aReq;
    }

    private ThreeDSRequestorAuthenticationInfo getThreeDSRequestorAuthenticationInfo(RBKMoneyAuthenticationRequest request) {
        Optional<ThreeDSRequestorAuthenticationInfoWrapper> wrapper = Optional.ofNullable(request.getThreeDSRequestorAuthenticationInfo());

        if (wrapper.isPresent()) {
            ThreeDSRequestorAuthenticationInfo threeDSRequestorAuthenticationInfo = new ThreeDSRequestorAuthenticationInfo();
            threeDSRequestorAuthenticationInfo.setThreeDSReqAuthData(wrapper.map(ThreeDSRequestorAuthenticationInfoWrapper::getThreeDSReqAuthData).orElse(null));
            threeDSRequestorAuthenticationInfo.setThreeDSReqAuthMethod(wrapper.map(ThreeDSRequestorAuthenticationInfoWrapper::getThreeDSReqAuthMethod).map(EnumWrapper::getValue).orElse(null));
            threeDSRequestorAuthenticationInfo.setThreeDSReqAuthTimestamp(wrapper.map(ThreeDSRequestorAuthenticationInfoWrapper::getThreeDSReqAuthTimestamp).map(TemporalAccessorWrapper::getValue).orElse(null));
            return threeDSRequestorAuthenticationInfo;
        } else {
            return null;
        }
    }

    private String getThreeDsServerUrl(RBKMoneyAuthenticationRequest request) {
        if (isMirCrutchCondition(request.getDeviceChannel().getValue(), rbkMoneyDsProviderHolder.getEnvironmentProperties())) {
            return null;
        } else {
            return rbkMoneyDsProviderHolder.getEnvironmentProperties().getThreeDsServerUrl();
        }
    }

    private MerchantRiskIndicator getMerchantRiskIndicator(RBKMoneyAuthenticationRequest request) {
        Optional<MerchantRiskIndicatorWrapper> wrapper = Optional.ofNullable(request.getMerchantRiskIndicator());

        if (wrapper.isPresent()) {
            MerchantRiskIndicator merchantRiskIndicator = new MerchantRiskIndicator();
            merchantRiskIndicator.setDeliveryEmailAddress(wrapper.map(MerchantRiskIndicatorWrapper::getDeliveryEmailAddress).orElse(null));
            merchantRiskIndicator.setDeliveryTimeframe(wrapper.map(MerchantRiskIndicatorWrapper::getDeliveryTimeframe).map(EnumWrapper::getValue).orElse(null));
            merchantRiskIndicator.setGiftCardAmount(wrapper.map(MerchantRiskIndicatorWrapper::getGiftCardAmount).orElse(null));
            merchantRiskIndicator.setGiftCardCount(wrapper.map(MerchantRiskIndicatorWrapper::getGiftCardCount).orElse(null));
            merchantRiskIndicator.setGiftCardCurr(wrapper.map(MerchantRiskIndicatorWrapper::getGiftCardCurr).orElse(null));
            merchantRiskIndicator.setPreOrderDate(wrapper.map(MerchantRiskIndicatorWrapper::getPreOrderDate).map(TemporalAccessorWrapper::getValue).orElse(null));
            merchantRiskIndicator.setPreOrderPurchaseInd(wrapper.map(MerchantRiskIndicatorWrapper::getPreOrderPurchaseInd).map(EnumWrapper::getValue).orElse(null));
            merchantRiskIndicator.setReorderItemsInd(wrapper.map(MerchantRiskIndicatorWrapper::getReorderItemsInd).map(EnumWrapper::getValue).orElse(null));
            merchantRiskIndicator.setShipIndicator(wrapper.map(MerchantRiskIndicatorWrapper::getShipIndicator).map(EnumWrapper::getValue).orElse(null));
            return merchantRiskIndicator;
        } else {
            return null;
        }
    }

    private AccountInfo getAcctInfo(RBKMoneyAuthenticationRequest request) {
        Optional<AccountInfoWrapper> wrapper = Optional.ofNullable(request.getAcctInfo());

        if (wrapper.isPresent()) {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setChAccAgeInd(wrapper.map(AccountInfoWrapper::getChAccAgeInd).map(EnumWrapper::getValue).orElse(null));
            accountInfo.setChAccChange(wrapper.map(AccountInfoWrapper::getChAccChange).map(TemporalAccessorWrapper::getValue).orElse(null));
            accountInfo.setChAccChangeInd(wrapper.map(AccountInfoWrapper::getChAccChangeInd).map(EnumWrapper::getValue).orElse(null));
            accountInfo.setChAccDate(wrapper.map(AccountInfoWrapper::getChAccDate).map(TemporalAccessorWrapper::getValue).orElse(null));
            accountInfo.setChAccPwChange(wrapper.map(AccountInfoWrapper::getChAccPwChange).map(TemporalAccessorWrapper::getValue).orElse(null));
            accountInfo.setChAccPwChangeInd(wrapper.map(AccountInfoWrapper::getChAccPwChangeInd).map(EnumWrapper::getValue).orElse(null));
            accountInfo.setNbPurchaseAccount(wrapper.map(AccountInfoWrapper::getNbPurchaseAccount).orElse(null));
            accountInfo.setProvisionAttemptsDay(wrapper.map(AccountInfoWrapper::getProvisionAttemptsDay).orElse(null));
            accountInfo.setTxnActivityDay(wrapper.map(AccountInfoWrapper::getTxnActivityDay).orElse(null));
            accountInfo.setTxnActivityYear(wrapper.map(AccountInfoWrapper::getTxnActivityYear).orElse(null));
            accountInfo.setPaymentAccAge(wrapper.map(AccountInfoWrapper::getPaymentAccAge).map(TemporalAccessorWrapper::getValue).orElse(null));
            accountInfo.setPaymentAccInd(wrapper.map(AccountInfoWrapper::getPaymentAccInd).map(EnumWrapper::getValue).orElse(null));
            accountInfo.setShipAddressUsage(wrapper.map(AccountInfoWrapper::getShipAddressUsage).map(TemporalAccessorWrapper::getValue).orElse(null));
            accountInfo.setShipAddressUsageInd(wrapper.map(AccountInfoWrapper::getShipAddressUsageInd).map(EnumWrapper::getValue).orElse(null));
            accountInfo.setShipNameIndicator(wrapper.map(AccountInfoWrapper::getShipNameIndicator).map(EnumWrapper::getValue).orElse(null));
            accountInfo.setSuspiciousAccActivity(wrapper.map(AccountInfoWrapper::getSuspiciousAccActivity).map(EnumWrapper::getValue).orElse(null));
            return accountInfo;
        } else {
            return null;
        }
    }

    private ThreeDSRequestorPriorAuthenticationInfo getThreeDSRequestorPriorAuthenticationInfo(RBKMoneyAuthenticationRequest request) {
        Optional<ThreeDSRequestorPriorAuthenticationInfoWrapper> wrapper = Optional.ofNullable(request.getThreeDSRequestorPriorAuthenticationInfo());

        if (wrapper.isPresent()) {
            ThreeDSRequestorPriorAuthenticationInfo info = new ThreeDSRequestorPriorAuthenticationInfo();
            info.setThreeDSReqPriorAuthData(wrapper.map(ThreeDSRequestorPriorAuthenticationInfoWrapper::getThreeDSReqPriorAuthData).orElse(null));
            info.setThreeDSReqPriorAuthMethod(wrapper.map(ThreeDSRequestorPriorAuthenticationInfoWrapper::getThreeDSReqPriorAuthMethod).map(EnumWrapper::getValue).orElse(null));
            info.setThreeDSReqPriorAuthTimestamp(wrapper.map(ThreeDSRequestorPriorAuthenticationInfoWrapper::getThreeDSReqPriorAuthTimestamp).map(TemporalAccessorWrapper::getValue).orElse(null));
            info.setThreeDSReqPriorRef(wrapper.map(ThreeDSRequestorPriorAuthenticationInfoWrapper::getThreeDSReqPriorRef).orElse(null));
            return info;
        } else {
            return null;
        }
    }

    private DeviceRenderOptions getDeviceRenderOptions(RBKMoneyAuthenticationRequest request) {
        Optional<DeviceRenderOptionsWrapper> wrapper = Optional.ofNullable(request.getDeviceRenderOptions());

        if (wrapper.isPresent()) {
            DeviceRenderOptions deviceRenderOptions = new DeviceRenderOptions();
            deviceRenderOptions.setSdkInterface(wrapper.map(DeviceRenderOptionsWrapper::getSdkInterface).map(EnumWrapper::getValue).orElse(null));
            wrapper
                    .map(DeviceRenderOptionsWrapper::getSdkUiType)
                    .map(ListWrapper::getValue)
                    .ifPresent(
                            list -> deviceRenderOptions.setSdkUiType(
                                    list.stream()
                                            .map(EnumWrapper::getValue)
                                            .collect(Collectors.toList())
                            )
                    );
            return deviceRenderOptions;
        } else {
            return null;
        }
    }

    private String getPurchaseInstalData(RBKMoneyAuthenticationRequest request) {
        if (getValue(request.getThreeDSRequestorAuthenticationInd()) == ThreeDSRequestorAuthenticationInd.INSTALMENT_TRANSACTION
                || getValue(request.getThreeRIInd()) == ThreeRIInd.INSTALMENT_TRANSACTION) {
            return request.getPurchaseInstalData();
        }
        return null;
    }

    private LocalDateTime getDecAuthMaxTime(RBKMoneyAuthenticationRequest request) {
        if (request.getThreeDSRequestorDecMaxTime() != null) {
            long threeDSRequestorDecMaxMinutes = Long.parseLong(request.getThreeDSRequestorDecMaxTime());
            return LocalDateTime.now(Clock.systemUTC())
                    .plusMinutes(threeDSRequestorDecMaxMinutes)
                    .plusSeconds(MAX_GRACE_PERIOD);
        }

        return null;
    }
}
