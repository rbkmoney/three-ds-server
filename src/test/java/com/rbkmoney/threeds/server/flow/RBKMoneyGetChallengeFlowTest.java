package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.MockConfig;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "preparation-flow.mode=RBK_MONEY_PLATFORM"})
@AutoConfigureMockMvc
public class RBKMoneyGetChallengeFlowTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdGenerator idGenerator;

    @Before
    public void setUp() {
        when(idGenerator.generateUUID()).thenReturn("bc9f0b90-1041-47f0-94df-d692170ea0d7");
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

        private static final String PATH = "message-flow/get-challenge/rbk-money-platform/";

        String incomingRequest() throws IOException {
            return readStringFromFile(PATH, "client-request.json");
        }

        String responseToClient() throws IOException {
            return readStringFromFile(PATH, "client-response.json");
        }
    }
}
