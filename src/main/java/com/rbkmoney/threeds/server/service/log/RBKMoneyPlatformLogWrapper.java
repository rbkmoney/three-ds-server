package com.rbkmoney.threeds.server.service.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyPlatformLogWrapper implements LogWrapper {

    private final DsProviderHolder dsProviderHolder;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void info(String message, Message data) {
        String dsId = dsProviderHolder.getTag(null);
        String jsonData = objectMapper.writeValueAsString(data);
        if (dsId != null) {
            log.info(String.format("%s: dsId=%s, %s", message, dsId, jsonData));
        } else {
            log.info(String.format("%s: %s", message, jsonData));
        }
    }

    @Override
    public void warn(String message, Throwable ex) {
        String dsId = dsProviderHolder.getTag(null);
        if (dsId != null) {
            log.warn(String.format("%s: dsId=%s", message, dsId), ex);
        } else {
            log.warn(message, ex);
        }
    }
}
