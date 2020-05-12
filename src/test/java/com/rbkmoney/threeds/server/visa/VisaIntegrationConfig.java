package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.domain.*;
import com.rbkmoney.threeds.server.domain.account.*;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptionsWrapper;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.domain.root.emvco.CReq;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.sdk.SdkInterface;
import com.rbkmoney.threeds.server.domain.sdk.SdkUiType;
import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
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
        classes = {ThreeDsServerApplication.class, TestConfig.class},
        properties = {
                "environment.ds-url=https://visasecuretestsuite-vsts.3dsecure.net/ds2",
                "client.ssl.trust-store=classpath:3ds_server_pki/visa.p12",
                "client.ssl.trust-store-password=Hd03tFk6iO3XiZ9a",
                "environment.three-ds-server-ref-number=3DS_LOA_SER_DIPL_020200_00236",
                "three-ds-server-url=https://visa.3ds.rbk.money",
                "logging.level.org.apache.http=debug",
        },
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@Ignore
public abstract class VisaIntegrationConfig {

    @Autowired
    protected SenderService senderService;

    @Autowired
    protected RestTemplate restTemplate;

    private static final Random RANDOM = new Random();

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
        pArq.getAcctInfo().setTxnActivityDay("1");
        pArq.getAcctInfo().setTxnActivityYear("1");
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

    protected void setMessageExtension(PArq pArq) {
        MessageExtension messageExtension = new MessageExtension();
        messageExtension.setId(UUID.randomUUID().toString());
        messageExtension.setName("kekich");
        messageExtension.setCriticalityIndicator(true);
        messageExtension.setData(Map.of("deviceChannel", pArq.getDeviceChannel().toString()));
        pArq.setMessageExtension(getListWrapper(List.of(messageExtension)));
    }

    protected EnumWrapper<ThreeDsMethodCompletionIndicator> randomThreeDsMethodCompletionIndicator() {
        return getEnumWrapper(ThreeDsMethodCompletionIndicator.SUCCESSFULLY_COMPLETED);
    }

    protected EnumWrapper<ThreeDSRequestorAuthenticationInd> randomThreeDSRequestorAuthenticationInd() {
        return getEnumWrapper(ThreeDSRequestorAuthenticationInd.ADD_CARD);
    }

    protected DeviceRenderOptionsWrapper randomDeviceRenderOptions() {
        return new DeviceRenderOptionsWrapper(getEnumWrapper(SdkInterface.NATIVE), getListWrapper(List.of(getEnumWrapper(SdkUiType.TEXT))));
    }

    protected String randomPurchaseExponent() {
        return "2";
    }

    protected String randomPurchaseCurrency() {
        return "643";
    }

    protected String randomMerchantCountryCode() {
        return "643";
    }

    protected String randomCardExpiryDate() {
        return "2012";
    }

    protected EnumWrapper<BrowserColorDepth> randomBrowserColorDepth() {
        return getEnumWrapper(BrowserColorDepth.BITS_4);
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
        return "https://www.visa.ru/notify";
    }

    protected String randomMail() {
        return "support@rbk.money";
    }

    protected Map<String, String> randomSdkEphemPubKey() {
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
        int leftLimit = 49; // letter '1'
        int rightLimit = 57; // letter '9'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
