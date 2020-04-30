package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class BRW_NPA_VisaIntegrationTest extends VisaIntegrationConfig {

    @Test
    public void test3DSS_210_301() {
        String acctNumber = "4012000000003077";
        DeviceChannel deviceChannel = DeviceChannel.BROWSER;
        String acquirerBIN = "400551";
        String messageVersion = "2.1.0";
        MessageCategory messageCategory = MessageCategory.NON_PAYMENT_AUTH;
        String threeDSServerOperatorID = "10075020";

        PArq pArq = buildPArq(acctNumber, deviceChannel, acquirerBIN, messageVersion, messageCategory, threeDSServerOperatorID);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertNull(pArs.getEci());
        assertNull(pArs.getAuthenticationValue());
    }

    private PArq buildPArq(String acctNumber, DeviceChannel deviceChannel, String acquirerBIN, String messageVersion, MessageCategory messageCategory, String threeDSServerOperatorID) {
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
                .merchantCountryCode(randomNumeric(3))
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(messageCategory))
                .notificationURL(randomUrl())
                .threeDSCompInd(randomThreeDsMethodCompletionIndicator())
                .threeDSRequestorAuthenticationInd(randomThreeDSRequestorAuthenticationInd())
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
