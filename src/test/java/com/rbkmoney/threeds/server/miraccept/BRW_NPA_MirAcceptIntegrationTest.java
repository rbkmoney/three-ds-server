package com.rbkmoney.threeds.server.miraccept;

import com.rbkmoney.threeds.server.domain.*;
import com.rbkmoney.threeds.server.domain.account.*;
import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class BRW_NPA_MirAcceptIntegrationTest extends MirAcceptIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.BROWSER;
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.NON_PAYMENT_AUTH;

    @Test
    public void testIssuerNotEnrolledOnDSNoAttempt_2_1() {
        PArq pArq = buildPArq("2201382000000062");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.INVALID_CARD_NUMBER, pArs.getTransStatusReason());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_1);
    }

    @Test
    public void testCardEnrolledFrictionlessFlowAuthenticated_2_3() {
        PArq pArq = buildPArq("2201382000000013");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("02", pArs.getEci());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_3);
    }

    @Test
    public void testCardEnrolledFrictionlessFlowNotAuthenticated_2_4() {
        PArq pArq = buildPArq("2201382000000021");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.CARD_AUTH_FAILED, pArs.getTransStatusReason());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_4);
    }

    @Test
    public void testCardEnrolledFrictionlessFlowRestricted_2_5() {
        PArq pArq = buildPArq("2201382000000005");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_REJECTED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.STOLEN_CARD, pArs.getTransStatusReason());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_5);
    }

    @Test
    public void testCardEnrolledChallengeFlowAuthenticated_2_6() {
        PArq pArq = buildPArq("2201382000000047");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_IS_NOT_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendAs3dsClientTypeBRW(pArs, submitWithCorrectPassword());

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_6);
    }

    @Test
    public void testCardEnrolledChallengeFlowNotAuthenticated_2_7() {
        PArq pArq = buildPArq("2201382000000054");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_IS_NOT_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendAs3dsClientTypeBRW(pArs, submitWithIncorrectPassword());

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED.getValue(), cRes.getTransStatus());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_7);
    }

    @Test
    public void testCardEnrolledFrictionlessFlowAllFieldsPresentedAuthenticated_2_9() {
        PArq pArq = buildPArq("2201382000000013");
        fullFilling(pArq);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("02", pArs.getEci());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_9);
    }

    @Test
    public void testCardEnrolledChallengeFlowAuthenticationCancelled_2_10() {
        PArq pArq = buildPArq("2201382000000047");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_IS_NOT_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendAs3dsClientTypeBRW(pArs, justCancel(), "01");

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED.getValue(), cRes.getTransStatus());

        writeInFileAppend(pArs, TestNumber.BRW_NPA_2_10);
    }

    private PArq buildPArq(String acctNumber) {
        PArq pArq = PArq.builder()
                .acctNumber(acctNumber)
                .acquirerBIN(ACQUIRER_BIN)
                .acquirerMerchantID(ACQUIRER_MERCHANT_ID)
                .browserAcceptHeader(randomString())
                .browserIP(randomIP())
                .browserColorDepth(getEnumWrapper(BrowserColorDepth.BITS_4))
                .browserJavaEnabled(randomBoolean())
                .browserLanguage(randomString(6))
                .browserScreenHeight(randomNumeric(4))
                .browserScreenWidth(randomNumeric(4))
                .browserTZ(randomNumeric(4))
                .browserUserAgent(randomString())
                .cardExpiryDate(CARD_EXPIRY_DATE)
                .deviceChannel(getEnumWrapper(DEVICE_CHANNEL))
                .mcc(randomNumeric(4))
                .merchantCountryCode("643")
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(MESSAGE_CATEGORY))
                .notificationURL(randomUrl())
                .threeDSCompInd(getEnumWrapper(ThreeDsMethodCompletionIndicator.SUCCESSFULLY_COMPLETED))
                .threeDSRequestorAuthenticationInd(getEnumWrapper(ThreeDSRequestorAuthenticationInd.ADD_CARD))
                .threeDSRequestorID(THREE_DS_REQUESTOR_ID)
                .threeDSRequestorName(randomString())
                .threeDSRequestorURL(randomUrl())
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .build();
        pArq.setMessageVersion(MESSAGE_VERSION);
        pArq.setUlTestCaseId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        return pArq;
    }

    protected void fullFilling(PArq pArq) {
        pArq.setAcctType(getEnumWrapper(AccountType.DEBIT));
        pArq.setAddrMatch(getEnumWrapper(AddressMatch.SAME_ADDRESS));
        pArq.getBillingAddress().setAddrCity(randomString());
        pArq.getBillingAddress().setAddrCountry("643");
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
        pArq.setMobilePhone(new Phone());
        pArq.getMobilePhone().setCc(randomNumeric(2));
        pArq.getMobilePhone().setSubscriber(randomNumeric(2));
        pArq.setPurchaseInstalData(randomNumericTwoNumbers());
        pArq.getShippingAddress().setAddrCity(randomString());
        pArq.getShippingAddress().setAddrCountry("643");
        pArq.getShippingAddress().setAddrLine1(randomString());
        pArq.getShippingAddress().setAddrLine2(randomString());
        pArq.getShippingAddress().setAddrLine3(randomString());
        pArq.getShippingAddress().setAddrPostCode(randomNumeric(5));
        pArq.getShippingAddress().setAddrState(randomNumeric(3));
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
        pArq.getThreeDSRequestorPriorAuthenticationInfo().setThreeDSReqPriorAuthData(randomString());
        pArq.getThreeDSRequestorPriorAuthenticationInfo().setThreeDSReqPriorAuthMethod(getEnumWrapper(ThreeDSReqPriorAuthMethod.OTHER_METHODS));
        pArq.getThreeDSRequestorPriorAuthenticationInfo().setThreeDSReqPriorAuthTimestamp(getTemporalAccessorWrapper(LocalDateTime.now()));
        pArq.getThreeDSRequestorPriorAuthenticationInfo().setThreeDSReqPriorRef(randomId());
    }
}