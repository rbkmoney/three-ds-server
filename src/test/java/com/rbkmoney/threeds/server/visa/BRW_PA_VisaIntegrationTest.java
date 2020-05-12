package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class BRW_PA_VisaIntegrationTest extends VisaIntegrationConfig {

    @Test
    public void test3DSS_210_101() {
        String acctNumber = "4012000000003010";
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

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
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

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
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

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
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

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
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

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
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID, threeDSRequestorAuthenticationInd);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.CHALLENGE_REQUIRED, pArs.getTransStatus());
        assertNotNull(pArs.getAcsURL());

        CRes cRes = sendAs3dsClientTypeBRW(pArs);

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL.getValue(), cRes.getTransStatus());
    }

    private PArq buildPArq(String acctNumber, DeviceChannel deviceChannel, String acquirerBIN, String messageVersion, MessageCategory messageCategory, String threeDSServerOperatorID, ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd) {
        PArq pArq = PArq.builder()
                .acctNumber(acctNumber)
                .acquirerBIN(acquirerBIN)
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
                .deviceChannel(getEnumWrapper(deviceChannel))
                .mcc(randomNumeric(4))
                .merchantCountryCode(randomMerchantCountryCode())
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(messageCategory))
                .notificationURL(randomUrl())
                .purchaseAmount(randomNumeric(5))
                .purchaseCurrency(randomPurchaseCurrency())
                .purchaseDate(randomLocalDateTime())
                .purchaseExponent(randomPurchaseExponent())
                .threeDSCompInd(randomThreeDsMethodCompletionIndicator())
                .threeDSRequestorAuthenticationInd(getEnumWrapper(threeDSRequestorAuthenticationInd))
                .threeDSRequestorID(randomNumeric(10))
                .threeDSRequestorName(randomString())
                .threeDSRequestorURL(randomUrl())
                .threeDSServerOperatorID(threeDSServerOperatorID)
                .build();
        pArq.setMessageVersion(messageVersion);
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        fullFilling(pArq);
        return pArq;
    }
}
