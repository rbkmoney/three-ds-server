package com.rbkmoney.threeds.server.config.testplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.client.impl.DsClientImpl;
import com.rbkmoney.threeds.server.config.builder.RestTemplateBuilder;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.holder.impl.DirectoryServerProviderTestPlatformHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "TEST_PLATFORM")
public class ClientConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }

    @Bean
    @RequestScope
    public DirectoryServerProviderHolder directoryServerProviderHolder(
            DsClient testDsClient,
            EnvironmentProperties testEnvironmentProperties) {
        return new DirectoryServerProviderTestPlatformHolder(
                testDsClient,
                testEnvironmentProperties);
    }

    @Bean
    @RequestScope
    public DsClient testDsClient(
            RestTemplate testRestTemplate,
            EnvironmentProperties testEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new DsClientImpl(
                testRestTemplate,
                testEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    @RequestScope
    public RestTemplate testRestTemplate(
            KeystoreProperties testKeystoreProperties,
            ResourceLoader resourceLoader,
            EnvironmentProperties testEnvironmentProperties) {
        return RestTemplateBuilder.restTemplate(
                testKeystoreProperties,
                resourceLoader,
                testEnvironmentProperties);
    }
}
