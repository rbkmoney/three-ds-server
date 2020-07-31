package com.rbkmoney.threeds.server.config.testplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.ds.client.DsClient;
import com.rbkmoney.threeds.server.ds.client.impl.TestPlatformDsClient;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.holder.impl.TestPlatformDsProviderHolder;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.LogWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import static com.rbkmoney.threeds.server.config.builder.RestTemplateBuilder.restTemplate;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class ClientConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }

    @Bean
    @RequestScope
    public DsProviderHolder dsProviderHolder(
            DsClient testDsClient,
            EnvironmentProperties testEnvironmentProperties) {
        return new TestPlatformDsProviderHolder(testDsClient, testEnvironmentProperties);
    }

    @Bean
    public DsClient testDsClient(
            RestTemplate testRestTemplate,
            EnvironmentProperties testEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            LogWrapper logWrapper) {
        return new TestPlatformDsClient(
                testRestTemplate,
                testEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver,
                logWrapper);
    }

    @Bean
    @RequestScope
    public RestTemplate testRestTemplate(
            EnvironmentProperties testEnvironmentProperties,
            KeystoreProperties testKeystoreProperties,
            ResourceLoader resourceLoader) {
        return restTemplate(testEnvironmentProperties, testKeystoreProperties, resourceLoader);
    }

    @Bean
    @ConfigurationProperties("environment.test")
    public EnvironmentProperties testEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("client.ds.ssl.test")
    public KeystoreProperties testKeystoreProperties() {
        return new KeystoreProperties();
    }
}
