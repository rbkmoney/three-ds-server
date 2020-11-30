package com.rbkmoney.threeds.server.ds.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.ds.DsClient;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestPlatformDsProviderHolder implements DsProviderHolder {

    private final TestPlatformDsClient testPlatformDsClient;

    private final EnvironmentProperties testEnvironmentProperties;

    @Override
    public DsClient getDsClient() {
        return testPlatformDsClient;
    }

    @Override
    public EnvironmentProperties getEnvironmentProperties() {
        return testEnvironmentProperties;
    }

}
