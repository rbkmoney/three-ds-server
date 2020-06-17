package com.rbkmoney.threeds.server.mastercard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.MockConfig;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.Data;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.Base64;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "storage.mode=IN_MEMORY",
                "platform.mode=TEST_PLATFORM",
                "preparation-flow.on-startup.enabled=false",
                "preparation-flow.on-schedule.enabled=false"})
@AutoConfigureMockMvc
public class MastercardPlatformTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdGenerator idGenerator;

    @Before
    public void setUp() {
        String threeDSServerTransID = "6f8ebd84-4dc0-4a2c-a783-3c7e180736a9";
//        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
//                .acsUrl("asd")
//                .build();
//
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);
//        when(cacheService.getChallengeFlowTransactionInfo(eq(threeDSServerTransID)))
//                .thenReturn(transactionInfo);
    }

    //    @Test
    public void asd() throws IOException {
//        String parq = "{\"messageType\":\"pArq\",\"messageVersion\":\"2.1.0\",\"acctNumber\":\"5204240692223900190\",\"cardExpiryDate\":\"2212\",\"deviceChannel\":{\"garbageValue\":null,\"value\":\"03\",\"garbage\":false},\"messageCategory\":{\"garbageValue\":null,\"value\":\"02\",\"garbage\":false},\"p_messageVersion\":\"1.0.5\",\"threeDSRequestorID\":\"709\",\"threeDSRequestorName\":\"EMVCo 3DS Test Requestor\",\"threeDSRequestorURL\":\"https://mictp.mastercard.int:45096/UL/08dc2cff-9b76-4562-a554-0a5a4b6c78fc\",\"billAddrCity\":\"City Name\",\"billAddrCountry\":\"840\",\"billAddrLine1\":\"Address Line 1\",\"billAddrLine2\":\"Address Line 2\",\"billAddrLine3\":\"Address Line 3\",\"billAddrPostCode\":\"Postal Code\",\"billAddrState\":\"AZ\",\"cardholderName\":\"Challenge One\",\"email\":\"example@example.com\",\"homePhone\":{\"cc\":\"123\",\"subscriber\":\"123456789\"},\"mobilePhone\":{\"cc\":\"123\",\"subscriber\":\"123456789\"},\"shipAddrCity\":\"City Name\",\"shipAddrCountry\":\"840\",\"shipAddrLine1\":\"Address Line 1\",\"shipAddrLine2\":\"Address Line 2\",\"shipAddrLine3\":\"Address Line 3\",\"shipAddrPostCode\":\"Postal Code\",\"shipAddrState\":\"AZ\",\"workPhone\":{\"cc\":\"123\",\"subscriber\":\"123456789\"},\"acctID\":\"EMVCo 3DS Test Account 000000001\",\"acctInfo\":{\"chAccAgeInd\":{\"garbageValue\":null,\"value\":\"05\",\"garbage\":false},\"chAccChange\":{\"garbageValue\":null,\"value\":[2017,1,1],\"garbage\":false},\"chAccChangeInd\":{\"garbageValue\":null,\"value\":\"04\",\"garbage\":false},\"chAccDate\":{\"garbageValue\":null,\"value\":[2017,1,1],\"garbage\":false},\"chAccPwChange\":{\"garbageValue\":null,\"value\":[2017,1,1],\"garbage\":false},\"chAccPwChangeInd\":{\"garbageValue\":null,\"value\":\"05\",\"garbage\":false},\"nbPurchaseAccount\":\"1\",\"provisionAttemptsDay\":\"0\",\"txnActivityDay\":\"1\",\"txnActivityYear\":\"1\",\"paymentAccAge\":{\"garbageValue\":null,\"value\":[2017,1,1],\"garbage\":false},\"paymentAccInd\":{\"garbageValue\":null,\"value\":\"05\",\"garbage\":false},\"shipAddressUsage\":{\"garbageValue\":null,\"value\":[2017,1,1],\"garbage\":false},\"shipAddressUsageInd\":{\"garbageValue\":null,\"value\":\"04\",\"garbage\":false},\"shipNameIndicator\":{\"garbageValue\":null,\"value\":\"01\",\"garbage\":false},\"suspiciousAccActivity\":{\"garbageValue\":null,\"value\":\"01\",\"garbage\":false}},\"acctType\":{\"garbageValue\":null,\"value\":\"02\",\"garbage\":false},\"merchantRiskIndicator\":{\"deliveryEmailAddress\":\"example@example.com\",\"deliveryTimeframe\":{\"garbageValue\":null,\"value\":\"02\",\"garbage\":false},\"giftCardAmount\":\"1\",\"giftCardCount\":\"01\",\"giftCardCurr\":\"840\",\"preOrderDate\":{\"garbageValue\":null,\"value\":[2030,1,1],\"garbage\":false},\"preOrderPurchaseInd\":{\"garbageValue\":null,\"value\":\"01\",\"garbage\":false},\"reorderItemsInd\":{\"garbageValue\":null,\"value\":\"01\",\"garbage\":false},\"shipIndicator\":{\"garbageValue\":null,\"value\":\"01\",\"garbage\":false}},\"threeRIInd\":\"04\",\"threeDSServerOperatorID\":\"threeDSServerOperatorUL\"}\n" +
//                "{\"messageType\":\"AReq\",\"messageVersion\":\"2.1.0\",\"threeDSRequestorID\":\"709\",\"threeDSRequestorName\":\"EMVCo 3DS Test Requestor\",\"threeDSRequestorURL\":\"https://mictp.mastercard.int:45096/UL/08dc2cff-9b76-4562-a554-0a5a4b6c78fc\",\"threeDSServerRefNumber\":\"3DS_LOA_SER_PPFU_020100_00008\",\"threeDSServerOperatorID\":\"threeDSServerOperatorUL\",\"threeDSServerTransID\":\"6f8ebd84-4dc0-4a2c-a783-3c7e180736a9\",\"threeDSServerURL\":\"https://3ds.rbk.money/ds\",\"threeRIInd\":\"04\",\"acctType\":\"02\",\"cardExpiryDate\":\"2212\",\"acctInfo\":{\"chAccAgeInd\":\"05\",\"chAccChange\":\"20170101\",\"chAccChangeInd\":\"04\",\"chAccDate\":\"20170101\",\"chAccPwChange\":\"20170101\",\"chAccPwChangeInd\":\"05\",\"nbPurchaseAccount\":\"1\",\"provisionAttemptsDay\":\"0\",\"txnActivityDay\":\"1\",\"txnActivityYear\":\"1\",\"paymentAccAge\":\"20170101\",\"paymentAccInd\":\"05\",\"shipAddressUsage\":\"20170101\",\"shipAddressUsageInd\":\"04\",\"shipNameIndicator\":\"01\",\"suspiciousAccActivity\":\"01\"},\"acctNumber\":\"5204240692223900190\",\"acctID\":\"EMVCo 3DS Test Account 000000001\",\"billAddrCity\":\"City Name\",\"billAddrCountry\":\"840\",\"billAddrLine1\":\"Address Line 1\",\"billAddrLine2\":\"Address Line 2\",\"billAddrLine3\":\"Address Line 3\",\"billAddrPostCode\":\"Postal Code\",\"billAddrState\":\"AZ\",\"email\":\"example@example.com\",\"homePhone\":{\"cc\":\"123\",\"subscriber\":\"123456789\"},\"mobilePhone\":{\"cc\":\"123\",\"subscriber\":\"123456789\"},\"cardholderName\":\"Challenge One\",\"shipAddrCity\":\"City Name\",\"shipAddrCountry\":\"840\",\"shipAddrLine1\":\"Address Line 1\",\"shipAddrLine2\":\"Address Line 2\",\"shipAddrLine3\":\"Address Line 3\",\"shipAddrPostCode\":\"Postal Code\",\"shipAddrState\":\"AZ\",\"workPhone\":{\"cc\":\"123\",\"subscriber\":\"123456789\"},\"deviceChannel\":\"03\",\"dsReferenceNumber\":\"3DS_LOA_DIS_PPFU_020100_00010\",\"dsTransID\":\"d657c4ad-920d-4000-8a7f-16b9ad5bd960\",\"merchantRiskIndicator\":{\"deliveryEmailAddress\":\"example@example.com\",\"deliveryTimeframe\":\"02\",\"giftCardAmount\":\"1\",\"giftCardCount\":\"01\",\"giftCardCurr\":\"840\",\"preOrderDate\":\"20300101\",\"preOrderPurchaseInd\":\"01\",\"reorderItemsInd\":\"01\",\"shipIndicator\":\"01\"},\"messageCategory\":\"02\"}";
//        System.out.println(parq);
//        PArq x = objectMapper.readValue(parq, PArq.class);
//        System.out.println(x);
//        Assert.assertNotNull(x.getThreeRIInd().getValue());
//        System.out.println(new FrictionlessFlow().asd("TC_SERVER_00011_001"));
        System.out.println(new FrictionlessFlow().readAReq("TC_SERVER_00011_001"));
        System.out.println(new FrictionlessFlow().readPArq("TC_SERVER_00011_001"));
        System.out.println(new FrictionlessFlow().readPArs("TC_SERVER_00011_001"));
        System.out.println(new FrictionlessFlow().readARes("TC_SERVER_00011_001"));
    }

    //    @Test
    public void TC_SERVER_00011_001() throws Exception {
//        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
//                .post(TEST_URL + "/sdk")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(new GetChallengeFlow().incomingRequest());
//
//        // When - Then
//        mockMvc.perform(prepRequest)
//                .andDo(print())
//                .andExpect(content()
//                        .json(new GetChallengeFlow().responseToClient()));
        String testCase = "TC_SERVER_00011_001";

        FrictionlessFlow frictionlessFlow = new FrictionlessFlow();

        stubFor(post(urlEqualTo("/"))
//                .withRequestBody(equalToJson(frictionlessFlow.readAReq(testCase)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBodyFile(frictionlessFlow.readARes(testCase))));

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post(TEST_URL + "/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(frictionlessFlow.requestToServer(testCase));

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(frictionlessFlow.responseFromServer(testCase)));
    }

    public class FrictionlessFlow {

        String requestToServer(String testCase) throws IOException {
            return readPArq(testCase);
        }

        String responseFromServer(String testCase) throws IOException {
            return readPArs(testCase);
        }

        private String readAReq(String testCase) throws IOException {
            return readMessage("mastercard/" + testCase + "/areq.json", AReq.class);
        }

        private String readARes(String testCase) throws IOException {
            return readMessage("mastercard/" + testCase + "/ares.json", ARes.class);
        }

        private String readPArq(String testCase) throws IOException {
            return readMessage("mastercard/" + testCase + "/parq.json", PArq.class);
        }

        private String readPArs(String testCase) throws IOException {
            return readMessage("mastercard/" + testCase + "/pars.json", PArs.class);
        }

        private <T> String readMessage(String fullPath, Class<T> valueType) throws IOException {
            Base64Message base64Message = readFromFile(fullPath, Base64Message.class);
            byte[] src = decodeBody(base64Message);
            T value = readValue(src, valueType);
            return writeValueAsString(value);
        }

        private byte[] decodeBody(Base64Message base64Message) {
            return Base64.getDecoder().decode(base64Message.getBody());
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(value = JsonInclude.Include.NON_ABSENT)
    public static class Base64Message {

        private String body;

    }
}
