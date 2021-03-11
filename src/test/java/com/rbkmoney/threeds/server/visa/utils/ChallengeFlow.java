package com.rbkmoney.threeds.server.visa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;

import java.util.Base64;

@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:localvariablename"})
public class ChallengeFlow {

    private final JsonMapper jsonMapper;

    public String requestFromDs(String testCase) {
        return readRReq(testCase);
    }

    public String responseToDs(String testCase) {
        return readRRes(testCase);
    }

    private String readRReq(String testCase) {
        return readMessage("visa/" + testCase + "/rreq.json");
    }

    private String readRRes(String testCase) {
        return readMessage("visa/" + testCase + "/rres.json");
    }

    private String readEncodeCReq(String testCase) {
        String cReq = readMessage("visa/" + testCase + "/creq.json");
        byte[] bytes = jsonMapper.writeValueAsBytes(cReq);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String readEncodeCRes(String testCase) {
        JsonNode jsonNode = jsonMapper.readValue(readMessage("visa/" + testCase + "/cres.json"), JsonNode.class);
        return jsonNode.toString();
    }

    private String readMessage(String fullPath) {
        return jsonMapper.readStringFromFile(fullPath);
    }
}
