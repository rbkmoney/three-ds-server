package com.rbkmoney.threeds.server.ds.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsClient;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
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
    public Optional<String> getTag(Message message) {
        return Optional.ofNullable(message).map(Message::getUlTestCaseId);
    }

    @Override
    public void setDsProvider(DsProvider dsProvider) {
    }

    @Override
    public Optional<String> getDsProvider() {
        return Optional.empty();
    }
}
