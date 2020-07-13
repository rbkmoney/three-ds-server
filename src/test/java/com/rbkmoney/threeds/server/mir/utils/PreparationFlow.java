package com.rbkmoney.threeds.server.mir.utils;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
public class PreparationFlow {

    private final JsonMapper jsonMapper;

    public void givenDsStub(String testCase) {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(requestToDsServer(testCase), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withHeader("x-ul-testcaserun-id", testCase)
                        .withBody(responseFromDsServer(testCase))));
    }

    public String requestToThreeDsServer(String testCase) {
        return readPPrq(testCase);
    }

    public String responseFromThreeDsServer(String testCase) {
        return readPPrs(testCase);
    }

    private String requestToDsServer(String testCase) {
        return readPReq(testCase);
    }

    private String responseFromDsServer(String testCase) {
        return readPRes(testCase);
    }

    private String readPPrq(String testCase) {
        return readMessage("mir/" + testCase + "/pprq.json");
    }

    private String readPPrs(String testCase) {
        return readMessage("mir/" + testCase + "/pprs.json");
    }

    private String readPReq(String testCase) {
        return readMessage("mir/" + testCase + "/preq.json");
    }

    private String readPRes(String testCase) {
        return readMessage("mir/" + testCase + "/pres.json");
    }

    private String readMessage(String fullPath) {
        return jsonMapper.readStringFromFile(fullPath);
    }
}
