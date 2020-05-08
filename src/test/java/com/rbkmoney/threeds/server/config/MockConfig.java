package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.client.impl.DsClientImpl;
import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;

import static com.rbkmoney.threeds.server.TestBase.TEST_URL;
import static java.util.Collections.emptySet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Need "spring.main.allow-bean-definition-overriding=true" for replacing beans
 */
@TestConfiguration
public class MockConfig {

    @Bean
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
    public EnvironmentProperties testEnvironmentProperties() {
        EnvironmentProperties environmentProperties = mock(EnvironmentProperties.class);

        when(environmentProperties.getDsUrl()).thenReturn(TEST_URL);
        when(environmentProperties.getThreeDsServerUrl()).thenReturn("https://3ds.rbk.money/ds");
        when(environmentProperties.getThreeDsServerRefNumber()).thenReturn("3DS_LOA_SER_PPFU_020100_00008");

        return environmentProperties;
    }

    @Bean
    public EnvironmentMessageProperties environmentMessageProperties() {
        EnvironmentMessageProperties environmentMessageProperties = mock(EnvironmentMessageProperties.class);
        when(environmentMessageProperties.getMessageVersion()).thenReturn("2.1.0");
        when(environmentMessageProperties.getPMessageVersion()).thenReturn("1.0.5");

        return environmentMessageProperties;
    }

    @Bean
    public Validator validator() {
        Validator mock = mock(Validator.class);
        when(mock.validate(any()))
                .thenReturn(emptySet());

        return mock;
    }

    @Bean
    public RestTemplate testRestTemplate() {
        return new RestTemplate();
    }
}
