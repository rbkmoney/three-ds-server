package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.BrowserColorDepth;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDsMethodCompletionIndicator;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VisaIntegrationTest extends VisaIntegrationConfig {

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
