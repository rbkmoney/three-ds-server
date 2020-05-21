package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class DirectoryServerProviderHolder {

    private final DsClient visaDsClient;
    private final DsClient mastercardDsClient;
    private final DsClient mirDsClient;
    private final DsClient testDsClient;

    private final EnvironmentProperties visaEnvironmentProperties;
    private final EnvironmentProperties mastercardEnvironmentProperties;
    private final EnvironmentProperties mirEnvironmentProperties;
    private final EnvironmentProperties testEnvironmentProperties;

    private DirectoryServerProvider provider;

    public DsClient getDsClient() {
        Objects.requireNonNull(provider, "Provider must be set!");

        log.debug("Return DsClient for provider={}", provider);
        switch (provider) {
            case VISA:
                return visaDsClient;
            case MASTERCARD:
                return mastercardDsClient;
            case MIR:
                return mirDsClient;
            case TEST:
                return testDsClient;
            default:
                throw new IllegalArgumentException("Unknown Directory Server provider: " + provider);
        }
    }

    public EnvironmentProperties getEnvironmentProperties() {
        Objects.requireNonNull(provider, "Provider must be set!");

        log.debug("Return EnvironmentProperties for provider={}", provider);
        switch (provider) {
            case VISA:
                return visaEnvironmentProperties;
            case MASTERCARD:
                return mastercardEnvironmentProperties;
            case MIR:
                return mirEnvironmentProperties;
            case TEST:
                return testEnvironmentProperties;
            default:
                throw new IllegalArgumentException("Unknown Directory Server provider: " + provider);
        }
    }

    public void setProvider(DirectoryServerProvider provider) {
        log.debug("Set provider={}", provider);
        this.provider = provider;
    }

    public String getTag(Message message) {
        return provider == DirectoryServerProvider.TEST
                ? message.getXULTestCaseRunId()
                : provider.getId();

    }
}
