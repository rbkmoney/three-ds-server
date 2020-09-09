package com.rbkmoney.threeds.server.flow.testplatform.challenge.happycase;

import com.rbkmoney.threeds.server.config.AbstractTestPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.flow.testplatform.challenge.ChallengeFlow;
import com.rbkmoney.threeds.server.service.CacheService;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class TestPlatformChallengeFlowTest extends AbstractTestPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private CacheService cacheService;

    @Test
    public void challengeFlowWithCardholderSelectedCancelTest() throws Exception {
        String testCase = "bc9f0b90-1041-47f0-94df-d692170ea0d7";
        String path = "flow/testplatform/challenge/happycase/cardholder-cancel/";
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .acsUrl("asd")
                .build();

        when(idGenerator.generateUUID()).thenReturn(testCase);
        when(cacheService.isInCardRange(anyString(), anyString())).thenReturn(true);
        doNothing().when(cacheService).saveChallengeFlowTransactionInfo(testCase, transactionInfo);
        when(cacheService.getChallengeFlowTransactionInfo(eq(testCase))).thenReturn(transactionInfo);

        ChallengeFlow challengeFlow = new ChallengeFlow(jsonMapper, path);

        // stub
        challengeFlow.givenDsStub(testCase);

        MockHttpServletRequestBuilder authRequest = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(challengeFlow.requestToThreeDsServer());

        mockMvc.perform(authRequest)
                .andDo(print())
                .andExpect(content()
                        .json(challengeFlow.responseFromThreeDsServer()));

        // rreq/rres flow w/o stub
        MockHttpServletRequestBuilder resultRequest = MockMvcRequestBuilders
                .post("/ds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(challengeFlow.requestFromDs());

        mockMvc.perform(resultRequest)
                .andDo(print())
                .andExpect(content()
                        .json(challengeFlow.responseToDs()));
    }

    @Test
    public void challengeFlowDefaultHandleTest() throws Exception {
        String testCase = "8c3595c6-8f26-4dfb-aff9-cd6a1f847567";
        String path = "flow/testplatform/challenge/happycase/default-handle/";
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .acsUrl("asd")
                .build();

        when(idGenerator.generateUUID()).thenReturn(testCase);
        when(cacheService.getChallengeFlowTransactionInfo(eq(testCase)))
                .thenReturn(transactionInfo);

        ChallengeFlow challengeFlow = new ChallengeFlow(jsonMapper, path);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(challengeFlow.requestPGcqToThreeDsServer());

        // When - Then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(challengeFlow.responsePGcsFromThreeDsServer()));
    }
}
