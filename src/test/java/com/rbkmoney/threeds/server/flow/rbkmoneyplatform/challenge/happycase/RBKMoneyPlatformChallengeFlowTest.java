package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.challenge.happycase;

import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.flow.rbkmoneyplatform.challenge.ChallengeFlow;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RBKMoneyPlatformChallengeFlowTest extends AbstractRBKMoneyPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private IdGenerator idGenerator;

    @Test
    public void challengeFlowDefaultHandleTest() throws Exception {
        String testCase = "bc9f0b90-1041-47f0-94df-d692170ea0d7";
        String path = "flow/rbkmoneyplatform/challenge/happycase/default-handle/";

        when(idGenerator.generateUUID()).thenReturn(testCase);

        ChallengeFlow challengeFlow = new ChallengeFlow(jsonMapper, path);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(challengeFlow.requestRGcqToThreeDsServer());

        // When - Then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(challengeFlow.responseRGcsFromThreeDsServer()));
    }
}
