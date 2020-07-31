package com.rbkmoney.threeds.server.ds.client.impl;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.LogWrapper;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RBKMoneyPlatformDsClient extends AbstractDsClient {

    public RBKMoneyPlatformDsClient(
            RestTemplate restTemplate,
            EnvironmentProperties environmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            LogWrapper logWrapper) {
        super(restTemplate, environmentProperties, messageToErrorConverter, errorCodeResolver, errorMessageResolver, logWrapper);
    }

    @Override
    public Message request(Message requestMessage) {
        try {
            return processHttpPost(requestMessage).getBody();
        } catch (ResourceAccessException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        } catch (RestClientException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        }
    }
}
