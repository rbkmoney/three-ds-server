package com.rbkmoney.threeds.server.ds.holder.impl;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.client.DsClient;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TestPlatformDsProviderHolder implements DsProviderHolder {

    private final DsClient testDsClient;

    private final EnvironmentProperties testEnvironmentProperties;

    @Override
    public DsClient getDsClient() {
        return testDsClient;
    }

    @Override
    public EnvironmentProperties getEnvironmentProperties() {
        return testEnvironmentProperties;
    }

    @Override
    public void setDsProvider(DsProvider dsProvider) {
    }

    @Override
    public String getTag(Message message) {
        return Optional.ofNullable(message).map(Message::getUlTestCaseId).orElse(null);
    }
}
