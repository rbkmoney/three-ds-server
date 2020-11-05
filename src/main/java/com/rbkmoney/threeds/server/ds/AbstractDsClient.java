package com.rbkmoney.threeds.server.ds;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractDsClient implements DsClient {

    private final RestTemplate restTemplate;
    private final EnvironmentProperties environmentProperties;
    private final Converter<ValidationResult, Message> messageToErrorResConverter;
    protected final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;
    private final LogWrapper logWrapper;

    @Override
    public void notifyDsAboutError(Erro message) {
        logWrapper.info("responseHandle return 'Erro', DS will be notified", message);

        ResponseEntity<Message> response = restTemplate.postForEntity(environmentProperties.getDsUrl(), message, Message.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logWrapper.info("responseHandle return 'Erro', DS was notified", message);
        } else {
            logWrapper.info("responseHandle return 'Erro', DS was NOT notified", message);
        }
    }

    protected ResponseEntity<Message> processHttpPost(Message requestMessage) {
        logWrapper.info("Request to DS", requestMessage);

        ResponseEntity<Message> responseMessageEntity = restTemplate.postForEntity(environmentProperties.getDsUrl(), requestMessage, Message.class);

        Message responseMessage = responseMessageEntity.getBody();

        logWrapper.info("Response from DS", responseMessage);

        responseMessage.setRequestMessage(requestMessage);

        return responseMessageEntity;
    }

    protected Message getMessage(RestClientException ex, Message request, ErrorCode errorCode) {
        logWrapper.warn("Cant receive response from DS", ex);
        return messageToErrorResConverter.convert(createFailureValidationResult(request, errorCode));
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
