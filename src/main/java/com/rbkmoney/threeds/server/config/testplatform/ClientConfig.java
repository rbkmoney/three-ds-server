package com.rbkmoney.threeds.server.config.testplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.config.builder.RestTemplateBuilder;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.testplatform.TestPlatformDsClient;
import com.rbkmoney.threeds.server.ds.testplatform.TestPlatformDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformLogWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

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
            TestPlatformDsClient testPlatformDsClient,
            EnvironmentProperties environmentProperties) {
        return new TestPlatformDsProviderHolder(testPlatformDsClient, environmentProperties);
    }

    @Bean
    public TestPlatformDsClient testPlatformDsClient(
            RestTemplate restTemplate,
            EnvironmentProperties environmentProperties,
            Converter<ValidationResult, Message> messageToErrorResConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            TestPlatformLogWrapper testPlatformLogWrapper) {
        return new TestPlatformDsClient(
                restTemplate,
                environmentProperties,
                messageToErrorResConverter,
                errorCodeResolver,
                errorMessageResolver,
                testPlatformLogWrapper);
    }

    @Bean
    @RequestScope
    public RestTemplate restTemplate(
            EnvironmentProperties environmentProperties,
            KeystoreProperties keystoreProperties,
            ResourceLoader resourceLoader) {
        return RestTemplateBuilder.restTemplate(environmentProperties, keystoreProperties, resourceLoader);
    }

    @Bean
    @ConfigurationProperties("environment.test")
    public EnvironmentProperties environmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("client.ds.ssl.test")
    public KeystoreProperties keystoreProperties() {
        return new KeystoreProperties();
    }
}
