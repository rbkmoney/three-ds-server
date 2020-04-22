package com.rbkmoney.threeds.server.miraccept;

import com.rbkmoney.threeds.server.domain.*;
import com.rbkmoney.threeds.server.domain.account.*;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.domain.root.emvco.CReq;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSReqAuthMethod;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInfoWrapper;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorPriorAuthenticationInfoWrapper;
import com.rbkmoney.threeds.server.domain.transaction.TransactionType;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.service.SenderService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
//                "environment.ds-url=https://ds1.mirconnect.ru:8443/ds/DServer",
                "environment.ds-url=https://ds.vendorcert.mirconnect.ru:8443/ds/DServer",
                "client.ssl.trust-store=classpath:3ds_server_pki/mir2.p12",
                "client.ssl.trust-store-password=vYOAkEF7V4UHLfMn",
                "environment.three-ds-server-ref-number=2200040105",
                "three-ds-server-url=https://nspk.3ds.rbk.money",
//                "logging.level.org.apache.http=debug",
        }
)
@Ignore
public abstract class MirAcceptIntegrationConfig {

    @Autowired
    protected SenderService senderService;

    @Autowired
    protected RestTemplate restTemplate;

    private static final Random RANDOM = new Random();

    //nspk
    protected static final String ACQUIRER_BIN = "220004";
    protected static final String ACQUIRER_MERCHANT_ID = "9295481501";
    protected static final String THREE_DS_REQUESTOR_ID = "2200040105";
    protected static final String THREE_DS_SERVER_OPERATOR_ID = "2200040105";
    protected static final String THREE_DS_SERVER_REF_NUMBER = "2200040105";

    protected static final String CARD_EXPIRY_DATE = "2012";
    protected static final String MERCHANT_COUNTRY_CODE = "643";
    protected static final String PURCHASE_CURRENCY = "643";
    protected static final String MESSAGE_VERSION = "2.1.0";

    protected void fullFilling(PArq pArq) {
        pArq.setAcctType(getEnumWrapper(AccountType.DEBIT));
        pArq.setAddrMatch(getEnumWrapper(AddressMatch.SAME_ADDRESS));
        pArq.getBillingAddress().setAddrCity(randomString());
        pArq.getBillingAddress().setAddrCountry(randomNumeric(3));
        pArq.getBillingAddress().setAddrLine1(randomString());
        pArq.getBillingAddress().setAddrLine2(randomString());
        pArq.getBillingAddress().setAddrLine3(randomString());
        pArq.getBillingAddress().setAddrPostCode(randomNumeric(5));
        pArq.getBillingAddress().setAddrState(randomNumeric(3));
        pArq.setCardholderName(randomString());
        pArq.setEmail(randomMail());
        pArq.setHomePhone(new Phone());
        pArq.getHomePhone().setCc(randomNumeric(2));
        pArq.getHomePhone().setSubscriber(randomNumeric(2));
        pArq.setMessageExtension(getListWrapper(List.of(new MessageExtension(randomString(), randomString(), false, Map.of(randomString(), randomString())))));
        pArq.setMobilePhone(new Phone());
        pArq.getMobilePhone().setCc(randomNumeric(2));
        pArq.getMobilePhone().setSubscriber(randomNumeric(2));
        pArq.setPurchaseInstalData(randomNumericTwoNumbers());
        pArq.setRecurringExpiry(randomLocalDate());
        pArq.setRecurringFrequency(randomNumeric(3));
        pArq.getShippingAddress().setAddrCity(randomString());
        pArq.getShippingAddress().setAddrCountry(randomNumeric(3));
        pArq.getShippingAddress().setAddrLine1(randomString());
        pArq.getShippingAddress().setAddrLine2(randomString());
        pArq.getShippingAddress().setAddrLine3(randomString());
        pArq.getShippingAddress().setAddrPostCode(randomNumeric(5));
        pArq.getShippingAddress().setAddrState(randomNumeric(3));
        pArq.setTransType(getEnumWrapper(TransactionType.CHECK_ACCEPTANCE));
        pArq.setWorkPhone(new Phone());
        pArq.getWorkPhone().setCc(randomNumeric(2));
        pArq.getWorkPhone().setSubscriber(randomNumeric(2));
        pArq.setAcctID(randomString());
        pArq.setAcctInfo(new AccountInfoWrapper());
        pArq.getAcctInfo().setChAccAgeInd(getEnumWrapper(ChAccAgeInd.FROM_30_TO_60_DAYS));
        pArq.getAcctInfo().setChAccChange(randomLocalDate());
        pArq.getAcctInfo().setChAccChangeInd(getEnumWrapper(ChAccChangeInd.FROM_30_TO_60_DAYS));
        pArq.getAcctInfo().setChAccDate(randomLocalDate());
        pArq.getAcctInfo().setChAccPwChange(randomLocalDate());
        pArq.getAcctInfo().setChAccPwChangeInd(getEnumWrapper(ChAccPwChangeInd.FROM_30_TO_60_DAYS));
        pArq.getAcctInfo().setNbPurchaseAccount(randomNumeric(3));
        pArq.getAcctInfo().setProvisionAttemptsDay(randomNumeric(3));
        pArq.getAcctInfo().setTxnActivityDay(randomNumeric(3));
        pArq.getAcctInfo().setTxnActivityYear(randomNumeric(3));
        pArq.getAcctInfo().setPaymentAccAge(randomLocalDate());
        pArq.getAcctInfo().setPaymentAccInd(getEnumWrapper(PaymentAccInd.FROM_30_TO_60_DAYS));
        pArq.getAcctInfo().setShipAddressUsage(randomLocalDate());
        pArq.getAcctInfo().setShipAddressUsageInd(getEnumWrapper(ShipAddressUsageInd.FROM_30_TO_60_DAYS));
        pArq.getAcctInfo().setShipNameIndicator(getEnumWrapper(ShipNameIndicator.ACCOUNT_NAME_DIFFERENT));
        pArq.getAcctInfo().setSuspiciousAccActivity(getEnumWrapper(SuspiciousAccActivity.SUSPICIOUS_ACTIVITY_OBSERVED));
        pArq.setMerchantRiskIndicator(new MerchantRiskIndicatorWrapper());
        pArq.getMerchantRiskIndicator().setDeliveryEmailAddress(randomMail());
        pArq.getMerchantRiskIndicator().setDeliveryTimeframe(getEnumWrapper(DeliveryTimeframe.ELECTRONIC_DELIVERY));
        pArq.getMerchantRiskIndicator().setGiftCardAmount(randomNumeric(10));
        pArq.getMerchantRiskIndicator().setGiftCardCount(randomNumeric(2));
        pArq.getMerchantRiskIndicator().setGiftCardCurr(randomNumeric(3));
        pArq.getMerchantRiskIndicator().setPreOrderDate(randomLocalDate());
        pArq.getMerchantRiskIndicator().setPreOrderPurchaseInd(getEnumWrapper(PreOrderPurchaseInd.FUTURE_AVAILABILITY));
        pArq.getMerchantRiskIndicator().setReorderItemsInd(getEnumWrapper(ReorderItemsInd.FIRST_TIME_ORDERED));
        pArq.getMerchantRiskIndicator().setShipIndicator(getEnumWrapper(ShipIndicator.ANOTHER_VERIFIED_ADDRESS));
        pArq.setThreeDSRequestorAuthenticationInfo(new ThreeDSRequestorAuthenticationInfoWrapper());
        pArq.getThreeDSRequestorAuthenticationInfo().setThreeDSReqAuthMethod(getEnumWrapper(ThreeDSReqAuthMethod.FEDERATED_ID));
        pArq.getThreeDSRequestorAuthenticationInfo().setThreeDSReqAuthTimestamp(randomLocalDateTime());
        pArq.getThreeDSRequestorAuthenticationInfo().setThreeDSReqAuthData(randomString());
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_MANDATE));
        pArq.setThreeDSRequestorPriorAuthenticationInfo(new ThreeDSRequestorPriorAuthenticationInfoWrapper());
    }

    protected CRes sendAs3dsClientTypeBRW(PArs pArs) {
        CReq cReq = CReq.builder()
                .acsTransID(pArs.getAcsTransID())
                .challengeWindowSize("05")
                .messageType("CReq")
                .messageVersion("2.1.0")
                .threeDSServerTransID(pArs.getThreeDSServerTransID())
                .build();

        return restTemplate.postForEntity(pArs.getAcsURL(), cReq, CRes.class).getBody();
    }

    protected CRes sendSetUpAs3dsClientTypeAPP(PArs pArs) {
        CReq cReq = CReq.builder()
                .acsTransID(pArs.getAcsTransID())
                .messageType("CReq")
                .messageVersion("2.1.0")
                .sdkTransID(pArs.getSdkTransID())
                .threeDSServerTransID(pArs.getThreeDSServerTransID())
                .sdkCounterStoA("001")
                .build();

        return restTemplate.postForEntity(pArs.getAcsURL(), cReq, CRes.class).getBody();
    }

    protected CRes sendAs3dsClientTypeAPP(PArs pArs) {
        CReq cReq = CReq.builder()
                .acsTransID(pArs.getAcsTransID())
                .messageType("CReq")
                .messageVersion("2.1.0")
                .sdkTransID(pArs.getSdkTransID())
                .threeDSServerTransID(pArs.getThreeDSServerTransID())
                .sdkCounterStoA("002")
                .challengeDataEntry(randomString())
                .build();

        return restTemplate.postForEntity(pArs.getAcsURL(), cReq, CRes.class).getBody();
    }

    protected CRes sendHTMLAs3dsClientTypeAPP(PArs pArs) {
        CReq cReq = CReq.builder()
                .acsTransID(pArs.getAcsTransID())
                .messageType("CReq")
                .messageVersion("2.1.0")
                .sdkTransID(pArs.getSdkTransID())
                .threeDSServerTransID(pArs.getThreeDSServerTransID())
                .sdkCounterStoA("002")
                .challengeCancel("01")
                .challengeHTMLDataEntry(randomString())
                .build();

        return restTemplate.postForEntity(pArs.getAcsURL(), cReq, CRes.class).getBody();
    }

    protected <T extends Valuable> EnumWrapper<T> getEnumWrapper(T value) {
        EnumWrapper<T> wrapper = new EnumWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected <T extends TemporalAccessor> TemporalAccessorWrapper<T> getTemporalAccessorWrapper(T value) {
        TemporalAccessorWrapper<T> wrapper = new TemporalAccessorWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected <T> ListWrapper<T> getListWrapper(List<T> value) {
        ListWrapper<T> wrapper = new ListWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected String randomId() {
        return UUID.randomUUID().toString();
    }

    protected boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    protected String randomIP() {
        return "192.168.0.1";
    }

    protected String randomUrl() {
        return "https://www.nspk.ru/notify";
    }

    protected String randomMail() {
        return "support@rbk.money";
    }

    protected Map<String, String> getSdkEphemPubKey() {
        return Map.of("crv", "P-256", "kty", "EC", "x", "bbeJ_uHcjTimTa13wahBrYqOOTruiadSImwMh-I9gMs", "y", "3p7hjQDYzw32iEfVFK_ixJ8iECl9bQ8_qmy7m64R4s8");
    }

    protected String randomNumericTwoNumbers() {
        return "66";
    }

    protected TemporalAccessorWrapper<LocalDate> randomLocalDate() {
        return getTemporalAccessorWrapper(LocalDate.now());
    }

    protected TemporalAccessorWrapper<LocalDateTime> randomLocalDateTime() {
        return getTemporalAccessorWrapper(LocalDateTime.now());
    }

    protected String randomString() {
        return randomString(10);
    }

    protected String randomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    protected String randomNumeric(int length) {
        int leftLimit = 48; // letter 'a'
        int rightLimit = 57; // letter 'z'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
