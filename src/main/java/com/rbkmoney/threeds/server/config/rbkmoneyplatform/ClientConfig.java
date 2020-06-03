package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.client.impl.DsClientImpl;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.holder.impl.DirectoryServerProviderRBKMoneyHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import static com.rbkmoney.threeds.server.config.builder.RestTemplateBuilder.restTemplate;

@Configuration
@ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "RBK_MONEY_PLATFORM")
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
            DsClient visaDsClient,
            DsClient mastercardDsClient,
            DsClient mirDsClient,
            EnvironmentProperties visaEnvironmentProperties,
            EnvironmentProperties mastercardEnvironmentProperties,
            EnvironmentProperties mirEnvironmentProperties) {
        return new DirectoryServerProviderRBKMoneyHolder(
                visaDsClient,
                mastercardDsClient,
                mirDsClient,
                visaEnvironmentProperties,
                mastercardEnvironmentProperties,
                mirEnvironmentProperties);
    }

    @Bean
    @RequestScope
    public DsClient visaDsClient(
            RestTemplate visaRestTemplate,
            EnvironmentProperties visaEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new DsClientImpl(
                visaRestTemplate,
                visaEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    @RequestScope
    public DsClient mastercardDsClient(
            RestTemplate mastercardRestTemplate,
            EnvironmentProperties mastercardEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new DsClientImpl(
                mastercardRestTemplate,
                mastercardEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    @RequestScope
    public DsClient mirDsClient(
            RestTemplate mirRestTemplate,
            EnvironmentProperties mirEnvironmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        return new DsClientImpl(
                mirRestTemplate,
                mirEnvironmentProperties,
                messageToErrorConverter,
                errorCodeResolver,
                errorMessageResolver);
    }

    @Bean
    @RequestScope
    public RestTemplate visaRestTemplate(
            KeystoreProperties visaKeystoreProperties,
            ResourceLoader resourceLoader,
            EnvironmentProperties visaEnvironmentProperties) {
        return restTemplate(
                visaKeystoreProperties,
                resourceLoader,
                visaEnvironmentProperties);
    }

    @Bean
    @RequestScope
    public RestTemplate mastercardRestTemplate(
            KeystoreProperties mastercardKeystoreProperties,
            ResourceLoader resourceLoader,
            EnvironmentProperties mastercardEnvironmentProperties) {
        return restTemplate(
                mastercardKeystoreProperties,
                resourceLoader,
                mastercardEnvironmentProperties);
    }

    @Bean
    @RequestScope
    public RestTemplate mirRestTemplate(
            KeystoreProperties mirKeystoreProperties,
            ResourceLoader resourceLoader,
            EnvironmentProperties mirEnvironmentProperties) {
        return restTemplate(
                mirKeystoreProperties,
                resourceLoader,
                mirEnvironmentProperties);
    }
}
