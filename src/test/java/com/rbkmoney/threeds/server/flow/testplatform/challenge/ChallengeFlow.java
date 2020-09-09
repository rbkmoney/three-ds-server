package com.rbkmoney.threeds.server.flow.testplatform.challenge;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
public class ChallengeFlow {

    private final JsonMapper jsonMapper;
    private final String path;

    public void givenDsStub(String testCase) {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "areq.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withHeader("x-ul-testcaserun-id", testCase)
                                .withBody(jsonMapper.readStringFromFile(path + "ares.json"))));
    }

    public String requestToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "parq.json");
    }

    public String responseFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pars.json");
    }

    public String requestFromDs() {
        return jsonMapper.readStringFromFile(path + "rreq.json");
    }

    public String responseToDs() {
        return jsonMapper.readStringFromFile(path + "rres.json");
    }

    public String requestPGcqToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pgcq.json");
    }

    public String responsePGcsFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "pgcs.json");
    }
}
