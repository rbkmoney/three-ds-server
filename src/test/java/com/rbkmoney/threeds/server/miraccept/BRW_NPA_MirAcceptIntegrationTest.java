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
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDsMethodCompletionIndicator;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

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
        // todo nspk assertNotNull(pArs.getAuthenticationValue());

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
        // todo nspk assertNotNull(pArs.getAuthenticationValue());

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

        CRes cRes = sendAs3dsClientTypeBRW(pArs, justCancel());

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
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        return pArq;
    }
}
