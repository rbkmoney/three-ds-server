package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.threedsmethod;

import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodData;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RBKMoneyThreeDsMethodTest extends AbstractRBKMoneyPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void shouldReturnThreeDsVersionIfAcctNumberIsSupportedThreeDsVersion() throws Exception {
        String expectedId = "1";
        ThreeDsMethodRequest threeDsMethodRequest = ThreeDsMethodRequest.builder()
                .threeDsMethodData(
                        ThreeDsMethodData.builder()
                                .threeDSServerTransID(expectedId)
                                .threeDSMethodNotificationURL("url1")
                                .build())
                .threeDsMethodUrl("url2")
                .build();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/three-ds-method")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(threeDsMethodRequest));

        mockMvc.perform(request)
                .andExpect(jsonPath("$.threeDsServerTransId").value(expectedId))
                .andExpect(jsonPath("$.htmlThreeDsMethodData").value(html()));
    }

    private String html() {
        return "<form name=\"ThreeDsMethodForm\"\n" +
                "      action=\"url2\"\n" +
                "      method=\"POST\">\n" +
                "    <input type=\"hidden\"\n" +
                "           name=\"threeDSMethodData\"\n" +
                "           value=\"eyJ0aHJlZURTU2VydmVyVHJhbnNJRCI6IjEiLCJ0aHJlZURTTWV0aG9kTm90aWZpY2F0aW9uVVJMIjoidXJsMSJ9\"\n" +
                "    />\n" +
                "</form>";
    }
}
