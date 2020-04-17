package com.rbkmoney.threeds.server.miraccept;

import com.rbkmoney.threeds.server.domain.BrowserColorDepth;
import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDsMethodCompletionIndicator;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class BRW_PA_MirAcceptIntegrationTest extends MirAcceptIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.BROWSER;
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.PAYMENT_AUTH;

    @Test
    public void testIssuerNotEnrolledOnDSNoAttempt_1_1() {
        PArq pArq = buildPArq("2201382000000062", "110000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.INVALID_CARD_NUMBER, pArs.getTransStatusReason());
    }

    @Test
    public void testCardNotEnrolledAttempt_1_2() {
        PArq pArq = buildPArq("2201382000000039", "120000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_ATTEMPTS_PERFORMED, pArs.getTransStatus());
        assertEquals("01", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowAuthenticated_1_3() {
        PArq pArq = buildPArq("2201382000000013", "130000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("02", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowNotAuthenticated_1_4() {
        PArq pArq = buildPArq("2201382000000021", "140000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.CARD_AUTH_FAILED, pArs.getTransStatusReason());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowRestricted_1_5() {
        PArq pArq = buildPArq("2201382000000005", "150000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_REJECTED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.STOLEN_CARD, pArs.getTransStatusReason());
    }

    @Test
    public void testCardEnrolledChallengeFlowAuthenticated_1_6() {
        PArq pArq = buildPArq("2201382000000047", "160000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_IS_NOT_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendAs3dsClientTypeBRW(pArs);

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());
    }

    @Test
    public void testCardEnrolledChallengeFlowNotAuthenticated_1_7() {
        PArq pArq = buildPArq("2201382000000054", "170000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_IS_NOT_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendAs3dsClientTypeBRW(pArs);

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED.getValue(), cRes.getTransStatus());
    }

    @Test
    public void testPreparationRequest_1_8() {
        PPrq pPrq = PPrq.builder()
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .threeDSServerTransID(randomId())
                .build();
        pPrq.setMessageVersion("2.1.0");
        pPrq.setXULTestCaseRunId(randomString());

        Message message = senderService.sendToDs(pPrq);

        assertTrue(message instanceof PPrs);

        PPrs pPrs = (PPrs) message;

        assertTrue(pPrs.getP_completed());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowAllFieldsPresentedAuthenticated_1_9() {
        PArq pArq = buildPArq("2201382000000013", "190000");
        fullFilling(pArq);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("02", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testCardEnrolledChallengeFlowAuthenticationCancelled_1_10() {
        PArq pArq = buildPArq("2201382000000047", "200000");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendAs3dsClientTypeBRW(pArs);

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED.getValue(), cRes.getTransStatus());
    }

    private PArq buildPArq(String acctNumber, String purchaseAmount) {
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
                .merchantCountryCode(MERCHANT_COUNTRY_CODE)
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(MESSAGE_CATEGORY))
                .notificationURL(randomUrl())
                .purchaseAmount(purchaseAmount)
                .purchaseCurrency(PURCHASE_CURRENCY)
                .purchaseDate(randomLocalDateTime())
                .purchaseExponent(randomNumeric(1))
                .threeDSCompInd(getEnumWrapper(ThreeDsMethodCompletionIndicator.SUCCESSFULLY_COMPLETED))
                .threeDSRequestorAuthenticationInd(getEnumWrapper(ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION))
                .threeDSRequestorID(THREE_DS_REQUESTOR_ID)
                .threeDSRequestorName(randomString())
                .threeDSRequestorURL(randomUrl())
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .build();
        pArq.setMessageVersion(MESSAGE_VERSION);
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        return pArq;
    }
}
