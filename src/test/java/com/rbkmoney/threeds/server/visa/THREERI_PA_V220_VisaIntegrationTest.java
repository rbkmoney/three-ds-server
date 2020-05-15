package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.*;

public class THREERI_PA_V220_VisaIntegrationTest extends VisaIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.THREE_REQUESTOR_INITIATED;
    private static final String ACQUIRER_BIN = "400551";
    private static final String MESSAGE_VERSION = "2.2.0";
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.PAYMENT_AUTH;
    private static final String THREE_DS_SERVER_OPERATOR_ID = "10075020";
    private static final ThreeDSRequestorAuthenticationInd THREE_DS_REQUESTOR_AUTHENTICATION_IND = ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;

    @Test
    public void testRequired3DSS_220_301() {
        String acctNumber = "4012000000001063";

        PArq pArq = buildPArq(acctNumber);

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("05", pArs.getEci());
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
                .threeRIInd(getEnumWrapper(ThreeRIInd.ADD_CARD))
                .build();
        pArq.setMessageVersion(MESSAGE_VERSION);
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        fullFilling(pArq);
        return pArq;
    }
}
