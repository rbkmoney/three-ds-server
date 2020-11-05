package com.rbkmoney.threeds.server.converter.testplatform;

import com.rbkmoney.threeds.server.domain.account.AccountInfo;
import com.rbkmoney.threeds.server.domain.account.AccountInfoWrapper;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptions;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptionsWrapper;
import com.rbkmoney.threeds.server.domain.merchant.MerchantRiskIndicator;
import com.rbkmoney.threeds.server.domain.merchant.MerchantRiskIndicatorWrapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
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
public class PArqToAReqConverter implements Converter<ValidationResult, Message> {

    /**
     * Seq 3.82 [Req 346] For a Decoupled Authentication transaction, at a minimum wait the
     * specified 3DS Requestor Decoupled Max Time plus 30 seconds for the RReq
     * message. If an RReq message is never received, further processing is outside
     * the scope of 3- Secure processing.
     */
    private static final int MAX_GRACE_PERIOD = 30;

    private final DsProviderHolder dsProviderHolder;
    private final IdGenerator idGenerator;

    @Override
    public Message convert(ValidationResult validationResult) {
        PArq pArq = (PArq) validationResult.getMessage();

        AReq aReq = AReq.builder()
                .threeDSCompInd(getValue(pArq.getThreeDSCompInd()))
                .threeDSRequestorAuthenticationInd(getValue(pArq.getThreeDSRequestorAuthenticationInd()))
                .threeDSRequestorAuthenticationInfo(getThreeDSRequestorAuthenticationInfo(pArq))
//              .threeDSReqAuthMethodInd(null) todo null? emvco: -
                .threeDSRequestorChallengeInd(getValue(pArq.getThreeDSRequestorChallengeInd()))
                .threeDSRequestorDecMaxTime(pArq.getThreeDSRequestorDecMaxTime())
                .threeDSRequestorDecReqInd(getValue(pArq.getThreeDSRequestorDecReqInd()))
                .threeDSRequestorID(pArq.getThreeDSRequestorID())
                .threeDSRequestorName(pArq.getThreeDSRequestorName())
                .threeDSRequestorPriorAuthenticationInfo(getThreeDSRequestorPriorAuthenticationInfo(pArq))
                .threeDSRequestorURL(pArq.getThreeDSRequestorURL())
                .threeDSServerRefNumber(dsProviderHolder.getEnvironmentProperties().getThreeDsServerRefNumber())
                .threeDSServerOperatorID(pArq.getThreeDSServerOperatorID())
                .threeDSServerTransID(idGenerator.generateUUID())
                .threeDSServerURL(getThreeDsServerUrl(pArq))
                .threeRIInd(getValue(pArq.getThreeRIInd()))
                .acctType(getValue(pArq.getAcctType()))
                .acquirerBIN(pArq.getAcquirerBIN())
                .acquirerMerchantID(pArq.getAcquirerMerchantID())
                .addrMatch(getValue(pArq.getAddrMatch()))
                .broadInfo(pArq.getBroadInfo())
                .browserAcceptHeader(pArq.getBrowserAcceptHeader())
                .browserIP(pArq.getBrowserIP())
                .browserJavaEnabled(pArq.getBrowserJavaEnabled())
                .browserJavascriptEnabled(pArq.getBrowserJavascriptEnabled())
                .browserLanguage(pArq.getBrowserLanguage())
                .browserColorDepth(getValue(pArq.getBrowserColorDepth()))
                .browserScreenHeight(pArq.getBrowserScreenHeight())
                .browserScreenWidth(pArq.getBrowserScreenWidth())
                .browserTZ(pArq.getBrowserTZ())
                .browserUserAgent(pArq.getBrowserUserAgent())
                .cardExpiryDate(pArq.getCardExpiryDate())
                .acctInfo(getAcctInfo(pArq))
                .acctNumber(pArq.getAcctNumber())
                .acctID(pArq.getAcctID())
                .billingAddress(
                        Address.builder()
                                .addrCity(pArq.getBillingAddress().getAddrCity())
                                .addrCountry(pArq.getBillingAddress().getAddrCountry())
                                .addrLine1(pArq.getBillingAddress().getAddrLine1())
                                .addrLine2(pArq.getBillingAddress().getAddrLine2())
                                .addrLine3(pArq.getBillingAddress().getAddrLine3())
                                .addrPostCode(pArq.getBillingAddress().getAddrPostCode())
                                .addrState(pArq.getBillingAddress().getAddrState())
                                .build()
                )
                .email(pArq.getEmail())
                .homePhone(pArq.getHomePhone())
                .mobilePhone(pArq.getMobilePhone())
                .cardholderName(pArq.getCardholderName())
                .shippingAddress(
                        Address.builder()
                                .addrCity(pArq.getShippingAddress().getAddrCity())
                                .addrCountry(pArq.getShippingAddress().getAddrCountry())
                                .addrLine1(pArq.getShippingAddress().getAddrLine1())
                                .addrLine2(pArq.getShippingAddress().getAddrLine2())
                                .addrLine3(pArq.getShippingAddress().getAddrLine3())
                                .addrPostCode(pArq.getShippingAddress().getAddrPostCode())
                                .addrState(pArq.getShippingAddress().getAddrState())
                                .build()
                )
                .workPhone(pArq.getWorkPhone())
                .deviceChannel(getValue(pArq.getDeviceChannel()))
//              .deviceInfo(null) emvco: source ds
                .deviceRenderOptions(getDeviceRenderOptions(pArq))
//              .dsReferenceNumber(null) emvco: source ds
//              .dsTransID(null) emvco: source ds
//              .dsURL(null) emvco: source ds
                .payTokenInd(pArq.getPayTokenInd())
                .payTokenSource(getValue(pArq.getPayTokenSource()))
                .purchaseInstalData(getPurchaseInstalData(pArq))
                .mcc(pArq.getMcc())
                .merchantCountryCode(pArq.getMerchantCountryCode())
                .merchantName(pArq.getMerchantName())
                .merchantRiskIndicator(getMerchantRiskIndicator(pArq))
                .messageCategory(getValue(pArq.getMessageCategory()))
                .messageExtension(getValue(pArq.getMessageExtension()))
                .notificationURL(pArq.getNotificationURL())
                .purchaseAmount(pArq.getPurchaseAmount())
                .purchaseCurrency(pArq.getPurchaseCurrency())
                .purchaseExponent(pArq.getPurchaseExponent())
                .purchaseDate(getValue(pArq.getPurchaseDate()))
                .recurringExpiry(getValue(pArq.getRecurringExpiry()))
                .recurringFrequency(pArq.getRecurringFrequency())
                .sdkAppID(pArq.getSdkAppID())
                .sdkEncData(pArq.getSdkEncData())
                .sdkEphemPubKey(pArq.getSdkEphemPubKey())
                .sdkMaxTimeout(pArq.getSdkMaxTimeout())
                .sdkReferenceNumber(pArq.getSdkReferenceNumber())
                .sdkTransID(pArq.getSdkTransID())
                .transType(getValue(pArq.getTransType()))
                .whiteListStatus(getValue(pArq.getWhiteListStatus()))
                .whiteListStatusSource(getValue(pArq.getWhiteListStatusSource()))
                .decoupledAuthMaxTime(getDecAuthMaxTime(pArq))
                .build();
        aReq.setMessageVersion(pArq.getMessageVersion());
        return aReq;
    }

    private ThreeDSRequestorAuthenticationInfo getThreeDSRequestorAuthenticationInfo(PArq pArq) {
        Optional<ThreeDSRequestorAuthenticationInfoWrapper> wrapper = Optional.ofNullable(pArq.getThreeDSRequestorAuthenticationInfo());

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

    private String getThreeDsServerUrl(PArq pArq) {
        if (isMirCrutchCondition(pArq.getDeviceChannel().getValue(), dsProviderHolder.getEnvironmentProperties())) {
            return null;
        } else {
            return dsProviderHolder.getEnvironmentProperties().getThreeDsServerUrl();
        }
    }

    private MerchantRiskIndicator getMerchantRiskIndicator(PArq pArq) {
        Optional<MerchantRiskIndicatorWrapper> wrapper = Optional.ofNullable(pArq.getMerchantRiskIndicator());

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

    private AccountInfo getAcctInfo(PArq pArq) {
        Optional<AccountInfoWrapper> wrapper = Optional.ofNullable(pArq.getAcctInfo());

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

    private ThreeDSRequestorPriorAuthenticationInfo getThreeDSRequestorPriorAuthenticationInfo(PArq pArq) {
        Optional<ThreeDSRequestorPriorAuthenticationInfoWrapper> wrapper = Optional.ofNullable(pArq.getThreeDSRequestorPriorAuthenticationInfo());

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

    private DeviceRenderOptions getDeviceRenderOptions(PArq pArq) {
        Optional<DeviceRenderOptionsWrapper> wrapper = Optional.ofNullable(pArq.getDeviceRenderOptions());

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

    private String getPurchaseInstalData(PArq pArq) {
        if (getValue(pArq.getThreeDSRequestorAuthenticationInd()) == ThreeDSRequestorAuthenticationInd.INSTALMENT_TRANSACTION
                || getValue(pArq.getThreeRIInd()) == ThreeRIInd.INSTALMENT_TRANSACTION) {
            return pArq.getPurchaseInstalData();
        }
        return null;
    }

    private LocalDateTime getDecAuthMaxTime(PArq pArq) {
        if (pArq.getThreeDSRequestorDecMaxTime() != null) {
            long threeDSRequestorDecMaxMinutes = Long.parseLong(pArq.getThreeDSRequestorDecMaxTime());
            return LocalDateTime.now(Clock.systemUTC())
                    .plusMinutes(threeDSRequestorDecMaxMinutes)
                    .plusSeconds(MAX_GRACE_PERIOD);
        }

        return null;
    }
}
