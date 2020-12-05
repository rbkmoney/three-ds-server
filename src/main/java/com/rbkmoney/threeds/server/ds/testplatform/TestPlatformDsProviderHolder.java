package com.rbkmoney.threeds.server.ds.testplatform;

import com.rbkmoney.threeds.server.ds.DsClient;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPlatformDsProviderHolder implements DsProviderHolder {

    private final TestPlatformDsClient testPlatformDsClient;

    @Override
    public DsClient getDsClient() {
        return testPlatformDsClient;
    }

}
