package com.rbkmoney.threeds.server.ds.client.impl;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import org.springframework.web.client.RestTemplate;

public class RBKMoneyPlatformDsClient extends AbstractDsClient {

    public RBKMoneyPlatformDsClient(
            RestTemplate restTemplate,
            EnvironmentProperties environmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        super(restTemplate, environmentProperties, messageToErrorConverter, errorCodeResolver, errorMessageResolver);
    }
}
