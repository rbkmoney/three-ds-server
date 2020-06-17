package com.rbkmoney.threeds.server.holder.impl;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DirectoryServerProviderTestPlatformHolder implements DirectoryServerProviderHolder {

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
    public void setProvider(DirectoryServerProvider provider) {
    }

    @Override
    public String getTag(Message message) {
        return message.getUlTestCaseId();
    }
}
