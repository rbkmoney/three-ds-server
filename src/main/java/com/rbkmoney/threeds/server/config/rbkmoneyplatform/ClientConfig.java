package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsClient;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyLogWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
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
    public DsProviderHolder dsProviderHolder(RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder) {
        return rbkMoneyDsProviderHolder;
    }

    @Bean
    @RequestScope
    public RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder(
            RBKMoneyDsClient visaDsClient,
            RBKMoneyDsClient mastercardDsClient,
            RBKMoneyDsClient mirDsClient,
            EnvironmentProperties visaEnvironmentProperties,
            EnvironmentProperties mastercardEnvironmentProperties,
            EnvironmentProperties mirEnvironmentProperties) {
        return new RBKMoneyDsProviderHolder(
                visaDsClient,
                mastercardDsClient,
                mirDsClient,
                visaEnvironmentProperties,
                mastercardEnvironmentProperties,
                mirEnvironmentProperties);
    }

    @Bean
    public RBKMoneyDsClient visaDsClient(
            RestTemplate visaRestTemplate,
            EnvironmentProperties visaEnvironmentProperties,
            Converter<ValidationResult, Message> messageToErrorResConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            RBKMoneyLogWrapper rbkMoneyLogWrapper) {
        return new RBKMoneyDsClient(
                visaRestTemplate,
                visaEnvironmentProperties,
                messageToErrorResConverter,
                errorCodeResolver,
                errorMessageResolver,
                rbkMoneyLogWrapper);
    }

    @Bean
    public RBKMoneyDsClient mastercardDsClient(
            RestTemplate mastercardRestTemplate,
            EnvironmentProperties mastercardEnvironmentProperties,
            Converter<ValidationResult, Message> messageToErrorResConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            RBKMoneyLogWrapper rbkMoneyLogWrapper) {
        return new RBKMoneyDsClient(
                mastercardRestTemplate,
                mastercardEnvironmentProperties,
                messageToErrorResConverter,
                errorCodeResolver,
                errorMessageResolver,
                rbkMoneyLogWrapper);
    }

    @Bean
    public RBKMoneyDsClient mirDsClient(
            RestTemplate mirRestTemplate,
            EnvironmentProperties mirEnvironmentProperties,
            Converter<ValidationResult, Message> messageToErrorResConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            RBKMoneyLogWrapper rbkMoneyLogWrapper) {
        return new RBKMoneyDsClient(
                mirRestTemplate,
                mirEnvironmentProperties,
                messageToErrorResConverter,
                errorCodeResolver,
                errorMessageResolver,
                rbkMoneyLogWrapper);
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
