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
    public void shouldReturnHtmlThreeDsMethodData() throws Exception {
        ThreeDsMethodRequest threeDsMethodRequest = ThreeDsMethodRequest.builder()
                .threeDsMethodData(
                        ThreeDsMethodData.builder()
                                .threeDSServerTransID("1")
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
                .andExpect(jsonPath("$.htmlThreeDsMethodData").value(html()));
    }

    private String html() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h2>RBK.money 3D Secure Method Form</h2>\n" +
                "\n" +
                "<form id=\"rbkMoneyThreeDsMethodForm\" name=\"ThreeDsMethodForm\"\n" +
                "      action=\"url2\"\n" +
                "      method=\"POST\">\n" +
                "    <input type=\"hidden\"\n" +
                "           name=\"threeDSMethodData\"\n" +
                "           value=\"eyJ0aHJlZURTU2VydmVyVHJhbnNJRCI6IjEiLCJ0aHJlZURTTWV0aG9kTm90aWZpY2F0aW9uVVJMIjoidXJsMSJ9\"\n" +
                "    />\n" +
                "</form>\n" +
                "\n" +
                "<script>\n" +
                "    document.getElementById(\"rbkMoneyThreeDsMethodForm\").submit()\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
