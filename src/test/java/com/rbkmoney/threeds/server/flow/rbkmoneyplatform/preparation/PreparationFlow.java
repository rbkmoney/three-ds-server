package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.preparation;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@RequiredArgsConstructor
public class PreparationFlow {

    private final JsonMapper jsonMapper;
    private final String path;

    public void givenDsStub() {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "preq.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(jsonMapper.readStringFromFile(path + "pres.json"))));
    }

    public String requestToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "rpq.json");
    }

    public String responseFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "rps.json");
    }
}
