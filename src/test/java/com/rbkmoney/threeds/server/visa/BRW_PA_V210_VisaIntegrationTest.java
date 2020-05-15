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
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class BRW_PA_V210_VisaIntegrationTest extends VisaIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.BROWSER;
    private static final String ACQUIRER_BIN = "400551";
    private static final String MESSAGE_VERSION = "2.1.0";
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.PAYMENT_AUTH;
    private static final String THREE_DS_SERVER_OPERATOR_ID = "10075020";
    private static final ThreeDSRequestorAuthenticationInd THREE_DS_REQUESTOR_AUTHENTICATION_IND = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

    @Test
    public void test3DSS_210_001() {
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
    public void test3DSS_210_101() {
        String acctNumber = "4012000000003010";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("05", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void test3DSS_210_102() {
        String acctNumber = "4012000000003028";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void test3DSS_210_103() {
        String acctNumber = "4012000000003036";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_ATTEMPTS_PERFORMED, pArs.getTransStatus());
        assertEquals("06", pArs.getEci());
        assertNotNull(pArs.getAuthenticationValue());
    }

    @Test
    public void test3DSS_210_104() {
        String acctNumber = "4012000000003044";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.TECHNICAL_PROBLEM, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void test3DSS_210_105() {
        String acctNumber = "4012000000003051";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_REJECTED, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    @Test
    public void test3DSS_210_106() {
        String acctNumber = "4012000000003101";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());

        CRes cRes = sendAs3dsRequestorWithTypeBRW(pArs);

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());
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
