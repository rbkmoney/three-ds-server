package com.rbkmoney.threeds.server.service.log;

import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyPlatformLogWrapper implements LogWrapper {

    private final DsProviderHolder dsProviderHolder;

    @Override
    public void info(String message, String data) {
        log.info(String.format("%s: directoryServerId=%s, %s", message, dsProviderHolder.getTag(null), data));
    }

    @Override
    public void warn(String message, Throwable ex) {
        log.warn(String.format("%s: directoryServerId=%s", message, dsProviderHolder.getTag(null)), ex);
    }
}
