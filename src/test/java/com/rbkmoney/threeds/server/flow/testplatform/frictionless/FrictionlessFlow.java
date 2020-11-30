package com.rbkmoney.threeds.server.flow.testplatform.frictionless;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
public class FrictionlessFlow {

    private final JsonMapper jsonMapper;
    private final String path;

    public void givenDsStub(String testCase) {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "areq.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withHeader("x-ul-testcaserun-id", testCase)
                                .withBody(jsonMapper.readStringFromFile(path + "ares.json"))));
    }

    public void givenErroDsStub(String testCase) {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "areq.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withHeader("x-ul-testcaserun-id", testCase)
                                .withBody(jsonMapper.readStringFromFile(path + "erro-ds.json"))));
    }

    public String requestToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "parq.json");
    }

    public String responseFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pars.json");
    }

    public String responseErroFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "erro-threeds.json");
    }
}
