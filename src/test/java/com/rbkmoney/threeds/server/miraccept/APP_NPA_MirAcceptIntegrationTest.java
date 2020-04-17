package com.rbkmoney.threeds.server.miraccept;

import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptionsWrapper;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.sdk.SdkInterface;
import com.rbkmoney.threeds.server.domain.sdk.SdkUiType;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class APP_NPA_MirAcceptIntegrationTest extends MirAcceptIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.APP_BASED;
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.NON_PAYMENT_AUTH;

    @Test
    public void testIssuerNotEnrolledOnDSNoAttempt_4_1() {
        PArq pArq = buildPArq("2201382000000062");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.INVALID_CARD_NUMBER, pArs.getTransStatusReason());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowAuthenticated_4_3() {
        PArq pArq = buildPArq("2201382000000013");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.LOW_CONFIDENCE, pArs.getTransStatusReason());
        assertEquals("02", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowNotAuthenticated_4_4() {
        PArq pArq = buildPArq("2201382000000021");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.CARD_AUTH_FAILED, pArs.getTransStatusReason());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowNotAuthenticated_4_5() {
        PArq pArq = buildPArq("2201382000000005");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_REJECTED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.STOLEN_CARD, pArs.getTransStatusReason());
    }

    @Test
    public void testCardEnrolledChallengeFlowAuthenticated_4_6() {
        PArq pArq = buildPArq("2201382000000047");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendSetUpAs3dsClientTypeAPP(pArs);

        assertEquals("N", cRes.getChallengeCompletionInd());
        assertEquals("CRes", cRes.getMessageType());
        assertEquals("2.1.0", cRes.getMessageVersion());
        assertEquals("05", cRes.getAcsUiType());
        assertEquals("001", cRes.getAcsCounterAtoS());

        CRes cResCompl = sendAs3dsClientTypeAPP(pArs);

        assertEquals("Y", cResCompl.getChallengeCompletionInd());
        assertEquals("CRes", cResCompl.getMessageType());
        assertEquals("2.1.0", cResCompl.getMessageVersion());
        assertEquals("Y", cResCompl.getTransStatus());
        assertEquals("002", cResCompl.getAcsCounterAtoS());
    }

    @Test
    public void testCardEnrolledChallengeFlowNotAuthenticated_4_6() {
        PArq pArq = buildPArq("2201382000000054");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendSetUpAs3dsClientTypeAPP(pArs);

        assertEquals("N", cRes.getChallengeCompletionInd());
        assertEquals("CRes", cRes.getMessageType());
        assertEquals("2.1.0", cRes.getMessageVersion());
        assertEquals("05", cRes.getAcsUiType());
        assertEquals("001", cRes.getAcsCounterAtoS());

        CRes cResCompl = sendAs3dsClientTypeAPP(pArs);

        assertEquals("Y", cResCompl.getChallengeCompletionInd());
        assertEquals("CRes", cResCompl.getMessageType());
        assertEquals("2.1.0", cResCompl.getMessageVersion());
        assertEquals("N", cResCompl.getTransStatus());
    }

    @Test
    public void testCardEnrolledFrictionlessFlowAllFieldsPresentedAuthenticated_4_9() {
        PArq pArq = buildPArq("2201382000000013");
        fullFilling(pArq);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.LOW_CONFIDENCE, pArs.getTransStatusReason());
        assertEquals("02", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testCardEnrolledChallengeFlowAuthenticationCancelled_4_10() {
        PArq pArq = buildPArq("2201382000000047");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());
        assertEquals(AuthenticationType.STATIC, pArs.getAuthenticationType());
        assertEquals(AcsChallengeMandated.CHALLENGE_MANDATED, pArs.getAcsChallengeMandated());

        CRes cRes = sendSetUpAs3dsClientTypeAPP(pArs);

        assertEquals("N", cRes.getChallengeCompletionInd());
        assertEquals("CRes", cRes.getMessageType());
        assertEquals("2.1.0", cRes.getMessageVersion());
        assertEquals("05", cRes.getAcsUiType());
        assertEquals("001", cRes.getAcsCounterAtoS());

        CRes cResCompl = sendHTMLAs3dsClientTypeAPP(pArs);

        assertEquals("Y", cResCompl.getChallengeCompletionInd());
        assertEquals("CRes", cResCompl.getMessageType());
        assertEquals("2.1.0", cResCompl.getMessageVersion());
        assertEquals("N", cResCompl.getTransStatus());
        assertEquals("002", cResCompl.getAcsCounterAtoS());
    }

    private PArq buildPArq(String acctNumber) {
        PArq pArq = PArq.builder()
                .acctNumber(acctNumber)
                .acquirerBIN(ACQUIRER_BIN)
                .acquirerMerchantID(ACQUIRER_MERCHANT_ID)
                .cardExpiryDate(CARD_EXPIRY_DATE)
                .deviceChannel(getEnumWrapper(DEVICE_CHANNEL))
                .deviceRenderOptions(new DeviceRenderOptionsWrapper(getEnumWrapper(SdkInterface.NATIVE), getListWrapper(List.of(getEnumWrapper(SdkUiType.TEXT)))))
                .mcc(randomNumeric(4))
                .merchantCountryCode(MERCHANT_COUNTRY_CODE)
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(MESSAGE_CATEGORY))
                .sdkAppID(randomId())
                .sdkEncData(randomString())
                .sdkEphemPubKey(getSdkEphemPubKey())
                .sdkMaxTimeout(randomNumericTwoNumbers())
                .sdkReferenceNumber(randomString())
                .sdkTransID(randomId())
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
