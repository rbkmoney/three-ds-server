package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.MockConfig;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.CacheService;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "platform.mode=TEST_PLATFORM"})
@AutoConfigureMockMvc
public class PgcqFlowTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private CacheService cacheService;

    @Before
    public void setUp() {
        String threeDSServerTransID = "8c3595c6-8f26-4dfb-aff9-cd6a1f847567";
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .acsUrl("asd")
                .build();

        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);
        when(cacheService.getChallengeFlowTransactionInfo(eq(threeDSServerTransID)))
                .thenReturn(transactionInfo);
    }

    @Test
    public void getChallengeFlowTest() throws Exception {
        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post(TEST_URL + "/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new GetChallengeFlow().incomingRequest());

        // When - Then
        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(new GetChallengeFlow().responseToClient()));
    }

    private class GetChallengeFlow {

        private static final String PATH = "message-flow/get-challenge/test-platform/";

        String incomingRequest() throws IOException {
            return readStringFromFile(PATH, "client-request.json");
        }

        String responseToClient() throws IOException {
            return readStringFromFile(PATH, "client-response.json");
        }
    }
}
