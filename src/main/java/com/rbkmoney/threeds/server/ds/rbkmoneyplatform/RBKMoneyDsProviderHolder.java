package com.rbkmoney.threeds.server.ds.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.ds.DsClient;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class RBKMoneyDsProviderHolder implements DsProviderHolder {

    private final Map<DsProvider, RBKMoneyDsClient> dsClientMap = new HashMap<>();

    private final Map<DsProvider, EnvironmentProperties> dsEnvironmentPropertiesMap = new HashMap<>();

    private final Map<DsProvider, Function<String, String>> threeDsRequestorIdMap = new HashMap<>();

    private final Map<DsProvider, Function<String, String>> threeDsRequestorNameMap = new HashMap<>();

    private DsProvider dsProvider;

    public RBKMoneyDsProviderHolder(
            RBKMoneyDsClient visaDsClient,
            RBKMoneyDsClient mastercardDsClient,
            RBKMoneyDsClient mirDsClient,
            EnvironmentProperties visaEnvironmentProperties,
            EnvironmentProperties mastercardEnvironmentProperties,
            EnvironmentProperties mirEnvironmentProperties) {
        dsClientMap.put(DsProvider.VISA, visaDsClient);
        dsClientMap.put(DsProvider.MASTERCARD, mastercardDsClient);
        dsClientMap.put(DsProvider.MIR, mirDsClient);

        threeDsRequestorIdMap
                .put(DsProvider.VISA, merchantId -> visaThreeDsRequestorId(visaEnvironmentProperties, merchantId));
        threeDsRequestorIdMap.put(DsProvider.MASTERCARD,
                merchantId -> mastercardThreeDsRequestorId(mastercardEnvironmentProperties, merchantId));
        threeDsRequestorIdMap
                .put(DsProvider.MIR, merchantId -> mirThreeDsRequestorId(mirEnvironmentProperties, merchantId));

        threeDsRequestorNameMap.put(DsProvider.VISA,
                merchantName -> visaThreeDsRequestorName(visaEnvironmentProperties, merchantName));
        threeDsRequestorNameMap.put(DsProvider.MASTERCARD,
                merchantName -> mastercardThreeDsRequestorName(mastercardEnvironmentProperties, merchantName));
        threeDsRequestorNameMap
                .put(DsProvider.MIR, merchantName -> mirThreeDsRequestorName(mirEnvironmentProperties, merchantName));

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

    public EnvironmentProperties getEnvironmentProperties() {
        Objects.requireNonNull(dsProvider, "Provider must be set!");

        log.debug("Return EnvironmentProperties for provider={}", dsProvider);

        return dsEnvironmentPropertiesMap.get(dsProvider);
    }

    public Optional<String> getDsProvider() {
        return Optional.ofNullable(dsProvider).map(DsProvider::getId);
    }

    public void setDsProvider(DsProvider dsProvider) {
        log.debug("Set dsProvider={}", dsProvider);

        this.dsProvider = dsProvider;
    }

    public String getThreeDsRequestorId(String merchantId) {
        Objects.requireNonNull(dsProvider, "Provider must be set!");

        return threeDsRequestorIdMap.get(dsProvider).apply(merchantId);
    }

    public String getThreeDsRequestorName(String merchantName) {
        Objects.requireNonNull(dsProvider, "Provider must be set!");

        return threeDsRequestorNameMap.get(dsProvider).apply(merchantName);
    }

    private String visaThreeDsRequestorId(EnvironmentProperties properties, String merchantId) {
        return merchantId.length() < 35
                ? properties.getThreeDsRequestorPrefix() + "*" + merchantId
                : properties.getThreeDsRequestorPrefix() + "*" + merchantId.substring(0, 35);
    }

    private String mastercardThreeDsRequestorId(EnvironmentProperties properties, String merchantId) {
        return properties.getThreeDsRequestorPrefix() + "_" + merchantId;
    }

    private String mirThreeDsRequestorId(EnvironmentProperties properties, String merchantId) {
        return properties.getThreeDsRequestorPrefix();
    }

    private String visaThreeDsRequestorName(EnvironmentProperties properties, String merchantName) {
        String s = merchantName.length() < 40 ? merchantName : merchantName.substring(0, 40);
        return s.replaceAll("\\s+", "");
    }

    private String mastercardThreeDsRequestorName(EnvironmentProperties properties, String merchantName) {
        String s = properties.getThreeDsRequestorPrefix() + "_" + merchantName;
        return s.replaceAll("\\s+", "");
    }

    private String mirThreeDsRequestorName(EnvironmentProperties properties, String merchantName) {
        return properties.getThreeDsRequestorPrefix();
    }
}
