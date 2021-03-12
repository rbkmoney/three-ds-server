package com.rbkmoney.threeds.server.flow.testplatform.preparation;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@RequiredArgsConstructor
public class PreparationFlow {

    private final JsonMapper jsonMapper;
    private final String path;

    public void givenFirstDsStub(String testCase) {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "preq.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withHeader("x-ul-testcaserun-id", testCase)
                                .withBody(jsonMapper.readStringFromFile(path + "pres.json"))));
    }

    public void givenSecondDsStub(String testCase) {
        reset();
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "preq-second.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withHeader("x-ul-testcaserun-id", testCase)
                                .withBody(jsonMapper.readStringFromFile(path + "pres-second.json"))));
    }

    public String requestToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pprq.json");
    }

    public String secondRequestToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pprq-second.json");
    }

    public String responseFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pprs.json");
    }
}
