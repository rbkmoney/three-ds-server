package com.rbkmoney.threeds.server.holder.impl;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DirectoryServerProviderRBKMoneyHolder implements DirectoryServerProviderHolder {

    private final Map<DirectoryServerProvider, DsClient> dsClientMap = new HashMap<>();

    private final Map<DirectoryServerProvider, EnvironmentProperties> dsEnvironmentPropertiesMap = new HashMap<>();

    private DirectoryServerProvider provider;

    public DirectoryServerProviderRBKMoneyHolder(
            DsClient visaDsClient,
            DsClient mastercardDsClient,
            DsClient mirDsClient,
            EnvironmentProperties visaEnvironmentProperties,
            EnvironmentProperties mastercardEnvironmentProperties,
            EnvironmentProperties mirEnvironmentProperties) {
        dsClientMap.put(DirectoryServerProvider.VISA, visaDsClient);
        dsClientMap.put(DirectoryServerProvider.MASTERCARD, mastercardDsClient);
        dsClientMap.put(DirectoryServerProvider.MIR, mirDsClient);

        dsEnvironmentPropertiesMap.put(DirectoryServerProvider.VISA, visaEnvironmentProperties);
        dsEnvironmentPropertiesMap.put(DirectoryServerProvider.MASTERCARD, mastercardEnvironmentProperties);
        dsEnvironmentPropertiesMap.put(DirectoryServerProvider.MIR, mirEnvironmentProperties);
    }

    @Override
    public DsClient getDsClient() {
        Objects.requireNonNull(provider, "Provider must be set!");

        log.debug("Return DsClient for provider={}", provider);

        return dsClientMap.get(provider);
    }

    @Override
    public EnvironmentProperties getEnvironmentProperties() {
        Objects.requireNonNull(provider, "Provider must be set!");

        log.debug("Return EnvironmentProperties for provider={}", provider);

        return dsEnvironmentPropertiesMap.get(provider);
    }

    @Override
    public void setProvider(DirectoryServerProvider provider) {
        log.debug("Set provider={}", provider);
        this.provider = provider;
    }

    @Override
    public String getTag(Message message) {
        return provider.getId();
    }
}
