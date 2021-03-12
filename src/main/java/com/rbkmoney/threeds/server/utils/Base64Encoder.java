package com.rbkmoney.threeds.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodData;
import com.rbkmoney.threeds.server.dto.CReq;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class Base64Encoder {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String encode(CReq cReq) {
        byte[] bytes = objectMapper.writeValueAsBytes(cReq);

        return Base64.getEncoder().encodeToString(bytes);
    }

    @SneakyThrows
    public String encode(ThreeDsMethodData threeDsMethodData) {
        byte[] bytes = objectMapper.writeValueAsBytes(threeDsMethodData);

        return Base64.getEncoder().encodeToString(bytes);
    }
}
