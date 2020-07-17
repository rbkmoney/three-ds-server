package com.rbkmoney.threeds.server.ds.client.impl;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
public class TestPlatformDsClient extends AbstractDsClient {

    public TestPlatformDsClient(
            RestTemplate restTemplate,
            EnvironmentProperties environmentProperties,
            MessageToErrorResConverter messageToErrorConverter,
            ErrorCodeResolver errorCodeResolver,
            ErrorMessageResolver errorMessageResolver) {
        super(restTemplate, environmentProperties, messageToErrorConverter, errorCodeResolver, errorMessageResolver);
    }

    @Override
    public Message request(Message requestMessage) {
        try {
            ResponseEntity<Message> responseMessageEntity = processHttpPost(requestMessage);

            Message responseMessage = responseMessageEntity.getBody();

            responseMessage.setUlTestCaseId(getUlTestCaseId(requestMessage, responseMessageEntity));

            return responseMessage;
        } catch (ResourceAccessException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        } catch (RestClientException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        }
    }

    private String getUlTestCaseId(Message requestMessage, ResponseEntity<Message> responseMessageEntity) {
        return Optional.ofNullable(responseMessageEntity.getHeaders().getFirst("x-ul-testcaserun-id"))
                .orElse(requestMessage.getUlTestCaseId());
    }
}
