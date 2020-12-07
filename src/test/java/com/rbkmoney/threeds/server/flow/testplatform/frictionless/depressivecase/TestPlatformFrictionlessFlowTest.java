package com.rbkmoney.threeds.server.flow.testplatform.frictionless.depressivecase;

import com.rbkmoney.threeds.server.config.AbstractTestPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.flow.testplatform.frictionless.FrictionlessFlow;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class TestPlatformFrictionlessFlowTest extends AbstractTestPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private IdGenerator idGenerator;

    @Test
    public void frictionlessFlowDefaultHandleTest() throws Exception {
        String testCase = "ab4c9b80-adcd-4421-af27-9549dc6c2f4b";
        String path = "flow/testplatform/frictionless/depressivecase/default-handle/";

        when(idGenerator.generateUUID()).thenReturn(testCase);

        FrictionlessFlow frictionlessFlow = new FrictionlessFlow(jsonMapper, path);

        // stub
        frictionlessFlow.givenErroDsStub(testCase);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(frictionlessFlow.requestToThreeDsServer());

        mockMvc.perform(request)
                .andExpect(content()
                        .json(frictionlessFlow.responseErroFromThreeDsServer()));
    }
}
