package com.rbkmoney.threeds.server.service.testplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TestPlatformLogWrapper implements LogWrapper {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void info(String message, Message data) {
        String jsonData = objectMapper.writeValueAsString(data);

        if (data instanceof PRes) {
            log.info("{}: {}", message, data.toString());
        } else {
            log.info("{}: {}", message, jsonData);
        }
    }

    @Override
    public void warn(String message, Throwable ex) {
        log.warn(message, ex);
    }
}

