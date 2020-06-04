package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.MockConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
public class DirectoryServerControllerTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void challengeFlowRReqRResPart() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TEST_URL + "/ds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ChallengeFlow().incomingRequestFromDs());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content()
                        .json(new ChallengeFlow().responseToDs()));
    }

    private class ChallengeFlow {

        private static final String PATH = "message-flow/challenge/cardholder-selected-cancel/first/";

        String incomingRequestFromDs() throws IOException {
            return readStringFromFile(PATH, "request-from-ds.json");
        }

        String responseToDs() throws IOException {
            return readStringFromFile(PATH, "response-to-ds.json");
        }
    }
}
