package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class BRW_PA_V220_VisaIntegrationTest extends VisaIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.BROWSER;
    private static final String ACQUIRER_BIN = "400551";
    private static final String MESSAGE_VERSION = "2.2.0";
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.PAYMENT_AUTH;
    private static final String THREE_DS_SERVER_OPERATOR_ID = "10075020";
    private static final ThreeDSRequestorAuthenticationInd THREE_DS_REQUESTOR_AUTHENTICATION_IND = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

    @Test
    public void testRequired3DSS_220_001() {
        PPrq pPrq = PPrq.builder()
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .threeDSServerTransID(randomId())
                .p_messageVersion("1.0.5")
                .threeDSRequestorID(randomNumeric(10))
                .threeDSRequestorURL(randomUrl())
                .build();
        pPrq.setMessageVersion(MESSAGE_VERSION);
        pPrq.setXULTestCaseRunId(randomString());

        Message message = senderService.sendToDs(pPrq);

        assertTrue(message instanceof PPrs);

        PPrs pPrs = (PPrs) message;

        assertTrue(pPrs.getP_completed());
    }

    @Test
    public void testOptional3DSS_220_002() {
        PPrq pPrq = PPrq.builder()
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .threeDSServerTransID(randomId())
                .p_messageVersion("1.0.5")
                .threeDSRequestorID(randomNumeric(10))
                .threeDSRequestorURL(randomUrl())
                .build();
        pPrq.setMessageVersion(MESSAGE_VERSION);
        pPrq.setXULTestCaseRunId(randomString());

        Message message = senderService.sendToDs(pPrq);

        assertTrue(message instanceof PPrs);

        PPrs pPrs = (PPrs) message;

        assertTrue(pPrs.getP_completed());

        message = senderService.sendToDs(pPrq);

        assertTrue(message instanceof PPrs);

        pPrs = (PPrs) message;

        assertTrue(pPrs.getP_completed());
    }

    @Test
    public void testRequired3DSS_220_101() {
        String acctNumber = "4012000000001006";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("05", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testRequired3DSS_220_102() {
        String acctNumber = "4012000000001014";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testRequired3DSS_220_103() {
        String acctNumber = "4012000000001022";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.TECHNICAL_PROBLEM, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testRequired3DSS_220_104() {
        String acctNumber = "4012000000001030";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_REJECTED, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testRequired3DSS_220_105() {
        String acctNumber = "4012000000001048";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_DATA_SHARE_ONLY));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.INFORMATIONAL_ONLY, pArs.getTransStatus());
        assertEquals("07", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testRequired3DSS_220_106() {
        String acctNumber = "4012000000001055";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_ATTEMPTS_PERFORMED, pArs.getTransStatus());
        assertEquals("06", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testRequired3DSS_220_107() {
        String acctNumber = "4012000000001097";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());

        CRes cRes = sendAs3dsRequestorWithTypeBRW(pArs);

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());
    }

    @Test
    public void testOptional3DSS_220_401() {
        String acctNumber = "4012000000001121";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.INFORMATIONAL_ONLY, pArs.getTransStatus());
        assertEquals("07", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_402() {
        String acctNumber = "4012000000001139";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764406");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.RESERVED_FOR_DS_USED_85, pArs.getTransStatusReason());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_403() {
        String acctNumber = "4012000000002004";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.RESERVED_FOR_DS_USED_88, pArs.getTransStatusReason());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_404() {
        String acctNumber = "4012000000003002";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.INFORMATIONAL_ONLY, pArs.getTransStatus());
        assertEquals("07", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_501() {
        String acctNumber = "4012000000001147";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_WHITELIST_PROMPT_REQUESTED));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());

        CRes cRes = sendAs3dsRequestorWithTypeBRW(pArs);

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());
    }

    @Test
    public void testOptional3DSS_220_502() {
        String acctNumber = "4012000000001188";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_UTILISE_WHITELIST_EXEMPTION));
        pArq.setWhiteListStatus(getEnumWrapper(WhiteListStatus.WHITELISTED));
        pArq.setWhiteListStatusSource(getEnumWrapper(WhiteListStatusSource.THREE_DS_SERVER));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("05", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_503() {
        String acctNumber = "4012000000001196";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764406");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_UTILISE_WHITELIST_EXEMPTION));
        pArq.setWhiteListStatus(getEnumWrapper(WhiteListStatus.WHITELISTED));
        pArq.setWhiteListStatusSource(getEnumWrapper(WhiteListStatusSource.THREE_DS_SERVER));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.RESERVED_FOR_DS_USED_85, pArs.getTransStatusReason());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_504() {
        String acctNumber = "4012000000002012";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_UTILISE_WHITELIST_EXEMPTION));
        pArq.setWhiteListStatus(getEnumWrapper(WhiteListStatus.WHITELISTED));
        pArq.setWhiteListStatusSource(getEnumWrapper(WhiteListStatusSource.THREE_DS_SERVER));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.RESERVED_FOR_DS_USED_88, pArs.getTransStatusReason());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void testOptional3DSS_220_505() {
        String acctNumber = "4012000000001154";

        PArq pArq = buildPArq(acctNumber);
        pArq.setThreeDSRequestorID("11764405");
        pArq.setThreeDSRequestorChallengeInd(getEnumWrapper(ThreeDSRequestorChallengeInd.NO_CHALLENGE_UTILISE_WHITELIST_EXEMPTION));
        pArq.setWhiteListStatus(getEnumWrapper(WhiteListStatus.WHITELISTED));
        pArq.setWhiteListStatusSource(getEnumWrapper(WhiteListStatusSource.THREE_DS_SERVER));

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
        assertEquals(WhiteListStatus.NOT_WHITELISTED, pArs.getWhiteListStatus());
        assertEquals(WhiteListStatusSource.DS, pArs.getWhiteListStatusSource());
    }

    @Test
    @Ignore
    // todo нет описания теста в user guide
    public void testOptional3DSS_220_601() {
        String acctNumber = "";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.INFORMATIONAL_ONLY, pArs.getTransStatus());
        assertEquals("07", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    @Ignore
    // todo нет описания теста в user guide
    public void testOptional3DSS_220_602() {
        String acctNumber = "";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    @Ignore
    // todo нет описания теста в user guide
    public void testOptional3DSS_220_603() {
        String acctNumber = "";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());

        CRes cRes = sendAs3dsRequestorWithTypeBRW(pArs);

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());
    }

    @Test
    @Ignore
    // todo нет описания теста в user guide
    public void testOptional3DSS_220_701() {
        String acctNumber = "";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.INFORMATIONAL_ONLY, pArs.getTransStatus());
        assertEquals("07", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    private PArq buildPArq(String acctNumber) {
        PArq pArq = PArq.builder()
                .acctNumber(acctNumber)
                .acquirerBIN(ACQUIRER_BIN)
                .acquirerMerchantID(randomNumeric(10))
                .browserAcceptHeader(randomString())
                .browserIP(randomIP())
                .browserColorDepth(randomBrowserColorDepth())
                .browserJavaEnabled(randomBoolean())
                .browserLanguage(randomString(6))
                .browserScreenHeight(randomNumeric(4))
                .browserScreenWidth(randomNumeric(4))
                .browserTZ(randomNumeric(4))
                .browserUserAgent(randomString())
                .browserJavascriptEnabled(randomBoolean())
                .cardExpiryDate(randomCardExpiryDate())
                .deviceChannel(getEnumWrapper(DEVICE_CHANNEL))
                .mcc(randomNumeric(4))
                .merchantCountryCode(randomMerchantCountryCode())
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(MESSAGE_CATEGORY))
                .notificationURL(randomUrl())
                .purchaseAmount(randomNumeric(5))
                .purchaseCurrency(randomPurchaseCurrency())
                .purchaseDate(randomLocalDateTime())
                .purchaseExponent(randomPurchaseExponent())
                .threeDSCompInd(randomThreeDsMethodCompletionIndicator())
                .threeDSRequestorAuthenticationInd(getEnumWrapper(THREE_DS_REQUESTOR_AUTHENTICATION_IND))
                .threeDSRequestorID(randomNumeric(10))
                .threeDSRequestorName(randomString())
                .threeDSRequestorURL(randomUrl())
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .build();
        pArq.setMessageVersion(MESSAGE_VERSION);
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        fullFilling(pArq);
        return pArq;
    }
}
