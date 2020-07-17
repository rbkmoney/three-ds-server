package com.rbkmoney.threeds.server.ds.client.impl;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.ds.client.DsClient;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyPlatformDsClient implements DsClient {

    private final RestTemplate restTemplate;
    private final EnvironmentProperties environmentProperties;
    private final MessageToErrorResConverter messageToErrorConverter;
    protected final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

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

    @Override
    public void notifyDsAboutError(Erro message) {
        log.info("Handling ended with Error result, DS will be notified: message={}", message.toString());

        ResponseEntity<Message> messageResponseEntity = restTemplate.postForEntity(environmentProperties.getDsUrl(), message, Message.class);

        log.info("Handling ended with Error result, DS was notified: responseCode={}, message={}", messageResponseEntity.getStatusCode(), message.toString());
    }

    protected ResponseEntity<Message> processHttpPost(Message requestMessage) {
        log.info("Send request message to DS: message={}", requestMessage.toString());

        ResponseEntity<Message> responseMessageEntity = restTemplate.postForEntity(environmentProperties.getDsUrl(), requestMessage, Message.class);

        Message responseMessage = responseMessageEntity.getBody();

        log.info("Receive response message from DS: message={}", responseMessage.toString());

        responseMessage.setRequestMessage(requestMessage);

        return responseMessageEntity;
    }

    protected Message getMessage(RestClientException ex, Message request, ErrorCode errorCode) {
        log.warn("Cant receive response from DS", ex);
        return messageToErrorConverter.convert(createFailureValidationResult(request, errorCode));
    }

    private ValidationResult createFailureValidationResult(Message request, ErrorCode errorCode) {
        return ValidationResult.failure(
                errorCode,
                errorMessageResolver.resolveDefaultErrorDetail(errorCode),
                errorMessageResolver.resolveDefaultErrorDescription(errorCode),
                request
        );
    }
}
