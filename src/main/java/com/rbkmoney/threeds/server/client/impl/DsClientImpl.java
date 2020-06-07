package com.rbkmoney.threeds.server.client.impl;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Slf4j
public class DsClientImpl implements DsClient {

    private final RestTemplate restTemplate;
    private final EnvironmentProperties environmentProperties;
    private final MessageToErrorResConverter messageToErrorConverter;
    private final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

    @Override
    public Message request(Message requestMessage) {
        try {
            log.info("Send request message to DS: message={}", requestMessage.toString());

            ResponseEntity<Message> responseMessageEntity = restTemplate.postForEntity(environmentProperties.getDsUrl(), requestMessage, Message.class);

            Message responseMessage = responseMessageEntity.getBody();

            requireNonNull(responseMessage);

            log.info("Receive response message from DS: message={}", responseMessage.toString());

            responseMessage.setRequestMessage(requestMessage);
            // todo remove or replace
            HttpHeaders headers = responseMessageEntity.getHeaders();
            responseMessage.setUlTestCaseId(getTestCaseRunId(requestMessage, headers));

            return responseMessage;
        } catch (ResourceAccessException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        } catch (RestClientException ex) {
            return getMessage(ex, requestMessage, errorCodeResolver.resolve(ex));
        }
    }

    @Override
    public void notificationDsAboutError(Erro message) {
        log.info("Handling ended with Error result, DS will be notified: message={}", message.toString());

        ResponseEntity<Message> messageResponseEntity = restTemplate.postForEntity(environmentProperties.getDsUrl(), message, Message.class);

        log.info("Handling ended with Error result, DS was notified: responseCode={}, message={}", messageResponseEntity.getStatusCode(), message.toString());
    }

    private Message getMessage(RestClientException ex, Message request, ErrorCode errorCode) {
        log.warn("Cant receive response from DS", ex);
        return messageToErrorConverter.convert(getValidationResult(request, errorCode));
    }

    private ValidationResult getValidationResult(Message request, ErrorCode errorCode) {
        return ValidationResult.failure(
                errorCode,
                errorMessageResolver.resolveDefaultErrorDetail(errorCode),
                errorMessageResolver.resolveDefaultErrorDescription(errorCode),
                request
        );
    }

    private String getTestCaseRunId(Message requestMessage, HttpHeaders headers) {
        return Optional
                .ofNullable(headers.getFirst("x-ul-testcaserun-id"))
                .orElse(requestMessage.getUlTestCaseId());
    }
}
