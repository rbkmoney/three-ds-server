package com.rbkmoney.threeds.server.ds.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsClient;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RBKMoneyDsProviderHolder implements DsProviderHolder {

    private final Map<DsProvider, DsClient> dsClientMap = new HashMap<>();

    private final Map<DsProvider, EnvironmentProperties> dsEnvironmentPropertiesMap = new HashMap<>();

    private DsProvider dsProvider;

    public RBKMoneyDsProviderHolder(
            DsClient visaDsClient,
            DsClient mastercardDsClient,
            DsClient mirDsClient,
            EnvironmentProperties visaEnvironmentProperties,
            EnvironmentProperties mastercardEnvironmentProperties,
            EnvironmentProperties mirEnvironmentProperties) {
        dsClientMap.put(DsProvider.VISA, visaDsClient);
        dsClientMap.put(DsProvider.MASTERCARD, mastercardDsClient);
        dsClientMap.put(DsProvider.MIR, mirDsClient);

        dsEnvironmentPropertiesMap.put(DsProvider.VISA, visaEnvironmentProperties);
        dsEnvironmentPropertiesMap.put(DsProvider.MASTERCARD, mastercardEnvironmentProperties);
        dsEnvironmentPropertiesMap.put(DsProvider.MIR, mirEnvironmentProperties);
    }

    @Override
    public DsClient getDsClient() {
        Objects.requireNonNull(dsProvider, "dsProvider must be set!");

        log.debug("Return DsClient for provider={}", dsProvider);

        return dsClientMap.get(dsProvider);
    }

    @Override
    public EnvironmentProperties getEnvironmentProperties() {
        Objects.requireNonNull(dsProvider, "Provider must be set!");

        log.debug("Return EnvironmentProperties for provider={}", dsProvider);

        return dsEnvironmentPropertiesMap.get(dsProvider);
    }

    @Override
    public Optional<String> getTag(Message message) {
        return getDsProvider();
    }

    public void setDsProvider(DsProvider dsProvider) {
        log.debug("Set dsProvider={}", dsProvider);

        this.dsProvider = dsProvider;
    }

    public Optional<String> getDsProvider() {
        return Optional.ofNullable(dsProvider).map(DsProvider::getId);
    }
}