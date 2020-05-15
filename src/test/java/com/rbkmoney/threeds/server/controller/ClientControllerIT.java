package com.rbkmoney.threeds.server.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.MockConfig;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.Before;
import org.junit.Test;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
public class ClientControllerIT extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheService cacheService;

    @MockBean
    private IdGenerator idGenerator;

    @Before
    public void setUp() {
        when(idGenerator.generateUUID())
                .thenReturn("bc9f0b90-1041-47f0-94df-d692170ea0d7");
    }

    @Test
    public void challengeFlowWithCardholderCancel() throws Exception {
        String xULTestCaseRunId = "bc9f0b90-1041-47f0-94df-d692170ea0d7";
        new ChallengeFlowCardholderSelectedCancel().givenDsStub();

        MockHttpServletRequestBuilder authRequest = MockMvcRequestBuilders
                .post(TEST_URL + "/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", xULTestCaseRunId)
                .content(new ChallengeFlowCardholderSelectedCancel().incomingRequest());

        mockMvc.perform(authRequest)
                .andDo(print())
                .andExpect(content()
                        .json(new ChallengeFlowCardholderSelectedCancel().responseToClient()));
    }

    @Test
    public void preparationFlow() throws Exception {
        String xULTestCaseRunId = "bc9f0b90-1041-47f0-94df-d692170ea0d7";
        assertNull(cacheService.getSerialNum(xULTestCaseRunId));
        assertTrue(cacheService.isInCardRange(xULTestCaseRunId, "7654320500000001"));

        new PreparationFlow().givenFirstDsResponseStub(xULTestCaseRunId);

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post(TEST_URL + "/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", xULTestCaseRunId)
                .content(new PreparationFlow().incomingRequest());

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(new PreparationFlow().responseToClient()));

        assertEquals("20190411083623719000", cacheService.getSerialNum(xULTestCaseRunId));
        assertFalse(cacheService.isInCardRange(xULTestCaseRunId, "7654320500000001"));

        new PreparationFlow().givenSecondDsResponseStub(xULTestCaseRunId);

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content().json(new PreparationFlow().responseToClient()));

        assertEquals("20190411083625236000", cacheService.getSerialNum(xULTestCaseRunId));
        assertTrue(cacheService.isInCardRange(xULTestCaseRunId, "7654320500000001"));

    }

    private class ChallengeFlowCardholderSelectedCancel {

        private static final String PATH = "message-flow/challenge/cardholder-selected-cancel/first/";

        String incomingRequest() throws IOException {
            return readStringFromFile(PATH, "incoming-request.json");
        }

        String responseToClient() throws IOException {
            return readStringFromFile(PATH, "response-to-client.json");
        }

        String responseFromDs() throws IOException {
            return readStringFromFile(PATH, "response-from-ds.json");
        }

        void givenDsStub() throws IOException {
            stubFor(post(urlEqualTo("/"))
                    .willReturn(
                            aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                    .withBody(responseFromDs())));
        }
    }

    private class PreparationFlow {

        private static final String PATH = "message-flow/preparation/populated-retain-service/";

        String incomingRequest() throws IOException {
            return readStringFromFile(PATH, "client-prep-request.json");
        }

        String responseToClient() throws IOException {
            return readStringFromFile(PATH, "client-prep-response.json");
        }

        void givenFirstDsResponseStub(String xULTestCaseRunId) throws IOException {
            stubFor(post(urlEqualTo("/"))
                    .willReturn(
                            aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                    .withHeader("x-ul-testcaserun-id", xULTestCaseRunId)
                                    .withBody(readStringFromFile(PATH, "preparation-response.json"))));
        }

        void givenSecondDsResponseStub(String xULTestCaseRunId) throws IOException {
            WireMock.reset();
            stubFor(post(urlEqualTo("/"))
                    .willReturn(
                            aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                    .withHeader("x-ul-testcaserun-id", xULTestCaseRunId)
                                    .withBody(readStringFromFile(PATH, "preparation-response-2.json"))));
        }
    }
}
