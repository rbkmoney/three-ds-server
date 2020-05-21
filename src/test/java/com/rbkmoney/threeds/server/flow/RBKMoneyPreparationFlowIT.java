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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "preparation-flow=RBK_MONEY"})
@AutoConfigureMockMvc
public class RBKMoneyPreparationFlowIT extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdGenerator idGenerator;

    @Before
    public void setUp() {
        when(idGenerator.generateUUID())
                .thenReturn("bc9f0b90-1041-47f0-94df-d692170ea0d7");
    }

    @Test
    public void preparationFlow() throws Exception {
        // Given
        new PreparationFlow().givenDsResponseStub();

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post(TEST_URL + "/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new PreparationFlow().incomingRequest());

        // When - Then
        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(new PreparationFlow().responseToClient()));
    }

    private class PreparationFlow {

        private static final String PATH = "message-flow/preparation/rbk-money/";

        String incomingRequest() throws IOException {
            return readStringFromFile(PATH, "client-prep-request.json");
        }

        String responseToClient() throws IOException {
            return readStringFromFile(PATH, "client-prep-response.json");
        }

        void givenDsResponseStub() throws IOException {
            stubFor(post(urlEqualTo("/"))
                    .willReturn(
                            aResponse()
                                    .withStatus(HttpStatus.OK.value())
                                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                    .withBody(readStringFromFile(PATH, "preparation-response.json"))));
        }
    }
}
