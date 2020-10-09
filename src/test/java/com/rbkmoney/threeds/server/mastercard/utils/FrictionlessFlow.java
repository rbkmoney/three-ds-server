package com.rbkmoney.threeds.server.mastercard.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.Base64;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
public class FrictionlessFlow {

    private final JsonMapper jsonMapper;
    private final Gson gson;

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
        return readPArq(testCase);
    }

    public String responseFromThreeDsServer(String testCase) {
        return readPArs(testCase);
    }

    private String requestToDsServer(String testCase) {
        return readAReq(testCase, "dsReferenceNumber", "dsTransID", "dsURL", "deviceInfo");
    }

    private String responseFromDsServer(String testCase) {
        return readARes(testCase);
    }

    private String readPArq(String testCase) {
        return readMessage("mastercard/" + testCase + "/parq.json");
    }

    private String readPArs(String testCase) {
        return readMessage("mastercard/" + testCase + "/pars.json");
    }

    private String readAReq(String testCase, String... removeProperties) {
        return removeFieldsFromOtherThreeDSComponents(
                readMessage("mastercard/" + testCase + "/areq.json"),
                removeProperties);
    }

    private String readARes(String testCase) {
        JsonObject parsJsonObject = gson.fromJson(readMessage("mastercard/" + testCase + "/pars.json"), JsonObject.class);

        JsonObject aresJsonObject = gson.fromJson(readMessage("mastercard/" + testCase + "/ares.json"), JsonObject.class);
        aresJsonObject.remove("authenticationValue");
        aresJsonObject.add("authenticationValue", parsJsonObject.get("authenticationValue"));
        return aresJsonObject.toString();
    }

    private String removeFieldsFromOtherThreeDSComponents(String json, String... removeProperties) {
        JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
        for (String removeProperty : removeProperties) {
            jsonObj.remove(removeProperty);
        }
        return jsonObj.toString();
    }

    private String readMessage(String fullPath) {
        Base64Message base64Message = jsonMapper.readFromFile(fullPath, Base64Message.class);
        byte[] src = decodeBody(base64Message);
        return new String(src, Charset.defaultCharset());
    }

    private byte[] decodeBody(Base64Message base64Message) {
        return Base64.getDecoder().decode(base64Message.getBody());
    }
}
