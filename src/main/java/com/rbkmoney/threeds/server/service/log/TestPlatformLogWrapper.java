package com.rbkmoney.threeds.server.service.log;

import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPlatformLogWrapper implements LogWrapper {

    @Override
    public void info(String message, String data) {
        log.info(String.format("%s: %s", message, data));
    }

    @Override
    public void warn(String message, Throwable ex) {
        log.warn(message, ex);
    }
}
