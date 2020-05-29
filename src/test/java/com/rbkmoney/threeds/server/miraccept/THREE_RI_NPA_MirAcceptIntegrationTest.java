package com.rbkmoney.threeds.server.miraccept;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class THREE_RI_NPA_MirAcceptIntegrationTest extends MirAcceptIntegrationConfig {

    private static final DeviceChannel DEVICE_CHANNEL = DeviceChannel.THREE_REQUESTOR_INITIATED;
    private static final MessageCategory MESSAGE_CATEGORY = MessageCategory.NON_PAYMENT_AUTH;

    private static final ThreeRIInd threeRIInd = ThreeRIInd.ACCOUNT_VERIFICATION;

    @Test
    public void testAccountVerificationCardnotEnrolledFrictionlessFlow_5_2() {
        PArq pArq = buildPArq("2201382000000039");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.TECHNICAL_PROBLEM, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.CARDHOLDER_NOT_ENROLLED, pArs.getTransStatusReason());

        writeInFileAppend(pArs, TestNumber.THREE_RI_NPA_5_2);
    }

    @Test
    public void testAccountVerificationCardEnrolledFrictionlessFlowAuthenticated_5_3() {
        PArq pArq = buildPArq("2201382000000013");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL, pArs.getTransStatus());
        assertEquals("02", pArs.getEci());

        writeInFileAppend(pArs, TestNumber.THREE_RI_NPA_5_3);
    }

    @Test
    public void testAccountVerificationCardEnrolledFrictionlessFlowNotAuthenticated_5_4() {
        PArq pArq = buildPArq("2201382000000021");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.NOT_AUTHENTICATED_DENIED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.CARD_AUTH_FAILED, pArs.getTransStatusReason());

        writeInFileAppend(pArs, TestNumber.THREE_RI_NPA_5_4);
    }

    @Test
    public void testAccountVerificationCardEnrolledFrictionlessFlowRestricted_5_5() {
        PArq pArq = buildPArq("2201382000000005");

        Message message = senderService.sendToDs(pArq);

        assertTrue(message instanceof PArs);

        PArs pArs = (PArs) message;

        assertEquals(TransactionStatus.AUTHENTICATION_REJECTED, pArs.getTransStatus());
        assertEquals(TransactionStatusReason.STOLEN_CARD, pArs.getTransStatusReason());

        writeInFileAppend(pArs, TestNumber.THREE_RI_NPA_5_5);
    }

    private PArq buildPArq(String acctNumber) {
        PArq pArq = PArq.builder()
                .acctNumber(acctNumber)
                .acquirerBIN(ACQUIRER_BIN)
                .acquirerMerchantID(ACQUIRER_MERCHANT_ID)
                .cardExpiryDate(CARD_EXPIRY_DATE)
                .deviceChannel(getEnumWrapper(DEVICE_CHANNEL))
                .mcc(randomNumeric(4))
                .merchantCountryCode(MERCHANT_COUNTRY_CODE)
                .merchantName(randomString())
                .messageCategory(getEnumWrapper(MESSAGE_CATEGORY))
                .threeDSRequestorID(THREE_DS_REQUESTOR_ID)
                .threeDSRequestorName(randomString())
                .threeDSRequestorURL(randomUrl())
                .threeDSServerOperatorID(THREE_DS_SERVER_OPERATOR_ID)
                .threeRIInd(getEnumWrapper(threeRIInd))
                .build();
        pArq.setMessageVersion(MESSAGE_VERSION);
        pArq.setXULTestCaseRunId(randomString());
        pArq.setBillingAddress(new Address());
        pArq.setShippingAddress(new Address());
        return pArq;
    }
}
