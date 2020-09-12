package com.rbkmoney.threeds.server.service.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.rbkmoney.threeds.server.utils.MessageUtils.empty;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyPlatformLogWrapper implements LogWrapper {

    private final DsProviderHolder dsProviderHolder;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void info(String message, Message data) {
        String dsProviderId = dsProviderHolder.getTag(data).orElse(null);
        String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        if (dsProviderId != null) {
            log.info(String.format("%s: dsProviderId=%s, \n%s", message, dsProviderId, jsonData));
        } else {
            log.info(String.format("%s: \n%s", message, jsonData));
        }
    }

    @Override
    public void warn(String message, Throwable ex) {
        String dsProviderId = dsProviderHolder.getTag(empty()).orElse(null);
        if (dsProviderId != null) {
            log.warn(String.format("%s: dsProviderId=%s", message, dsProviderId), ex);
        } else {
            log.warn(message, ex);
        }
    }
}