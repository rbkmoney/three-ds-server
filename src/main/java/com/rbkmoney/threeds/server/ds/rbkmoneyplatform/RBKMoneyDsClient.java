package com.rbkmoney.threeds.server.ds.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.AbstractDsClient;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.LogWrapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RBKMoneyDsClient extends AbstractDsClient {

    public RBKMoneyDsClient(
            RestTemplate restTemplate,
            EnvironmentProperties environmentProperties,
            Converter<ValidationResult, Message> messageToErrorResConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver,
            LogWrapper logWrapper) {
        super(restTemplate, environmentProperties, messageToErrorResConverter, errorCodeResolver, errorMessageResolver, logWrapper);
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
