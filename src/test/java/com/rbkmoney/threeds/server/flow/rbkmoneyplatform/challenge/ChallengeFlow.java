package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.challenge;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChallengeFlow {

    private final JsonMapper jsonMapper;
    private final String path;

    public String requestRGcqToThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "rgcq.json");
    }

    public String responseRGcsFromThreeDsServer() {
        return jsonMapper.readStringFromFile(path + "rgcs.json");
    }
}
