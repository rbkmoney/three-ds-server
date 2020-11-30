package com.rbkmoney.threeds.server.ds.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.AbstractDsClient;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyLogWrapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RBKMoneyDsClient extends AbstractDsClient {

    private final RBKMoneyLogWrapper rbkMoneyLogWrapper;

    public RBKMoneyDsClient(
            RestTemplate restTemplate,
            EnvironmentProperties environmentProperties,
            Converter<ValidationResult, Message> messageToErrorResConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver, RBKMoneyLogWrapper rbkMoneyLogWrapper) {
        super(restTemplate, environmentProperties, messageToErrorResConverter, errorCodeResolver, errorMessageResolver);
        this.rbkMoneyLogWrapper = rbkMoneyLogWrapper;
    }

    @Override
    public Message request(Message requestMessage) {
        try {
            return processRequest(requestMessage).getBody();
        } catch (ResourceAccessException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        } catch (RestClientException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        }
    }

    @Override
    protected void info(String message, Message data) {
        rbkMoneyLogWrapper.info(message, data);
    }

    @Override
    protected void warn(String message, Throwable ex) {
        rbkMoneyLogWrapper.warn(message, ex);
    }
}
