package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.ds.client.DsClient;
import com.rbkmoney.threeds.server.ds.client.impl.RBKMoneyPlatformDsClient;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.holder.impl.RBKMoneyPlatformDsProviderHolder;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
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
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
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
            DsClient visaDsClient,
            DsClient mastercardDsClient,
            DsClient mirDsClient,
            EnvironmentProperties visaEnvironmentProperties,
            EnvironmentProperties mastercardEnvironmentProperties,
            EnvironmentProperties mirEnvironmentProperties) {
        return new RBKMoneyPlatformDsProviderHolder(
                visaDsClient,
                mastercardDsClient,
                mirDsClient,
                visaEnvironmentProperties,
                mastercardEnvironmentProperties,
                mirEnvironmentProperties);
    }

    @Bean
    public DsClient visaDsClient(
            RestTemplate visaRestTemplate,
            EnvironmentProperties visaEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new RBKMoneyPlatformDsClient(
                visaRestTemplate,
                visaEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    public DsClient mastercardDsClient(
            RestTemplate mastercardRestTemplate,
            EnvironmentProperties mastercardEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new RBKMoneyPlatformDsClient(
                mastercardRestTemplate,
                mastercardEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    public DsClient mirDsClient(
            RestTemplate mirRestTemplate,
            EnvironmentProperties mirEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new RBKMoneyPlatformDsClient(
                mirRestTemplate,
                mirEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    @RequestScope
    public RestTemplate visaRestTemplate(
            EnvironmentProperties visaEnvironmentProperties,
            KeystoreProperties visaKeystoreProperties,
            ResourceLoader resourceLoader) {
        return restTemplate(visaEnvironmentProperties, visaKeystoreProperties, resourceLoader);
    }

    @Bean
    @RequestScope
    public RestTemplate mastercardRestTemplate(
            EnvironmentProperties mastercardEnvironmentProperties,
            KeystoreProperties mastercardKeystoreProperties,
            ResourceLoader resourceLoader) {
        return restTemplate(mastercardEnvironmentProperties, mastercardKeystoreProperties, resourceLoader);
    }

    @Bean
    @RequestScope
    public RestTemplate mirRestTemplate(
            EnvironmentProperties mirEnvironmentProperties,
            KeystoreProperties mirKeystoreProperties,
            ResourceLoader resourceLoader) {
        return restTemplate(mirEnvironmentProperties, mirKeystoreProperties, resourceLoader);
    }

    @Bean
    @ConfigurationProperties("environment.visa")
    public EnvironmentProperties visaEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("environment.mastercard")
    public EnvironmentProperties mastercardEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("environment.mir")
    public EnvironmentProperties mirEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("client.ds.ssl.visa")
    public KeystoreProperties visaKeystoreProperties() {
        return new KeystoreProperties();
    }

    @Bean
    @ConfigurationProperties("client.ds.ssl.mastercard")
    public KeystoreProperties mastercardKeystoreProperties() {
        return new KeystoreProperties();
    }

    @Bean
    @ConfigurationProperties("client.ds.ssl.mir")
    public KeystoreProperties mirKeystoreProperties() {
        return new KeystoreProperties();
    }
}
