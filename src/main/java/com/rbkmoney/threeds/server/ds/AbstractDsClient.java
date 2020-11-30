package com.rbkmoney.threeds.server.ds;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class AbstractDsClient implements DsClient {

    private final RestTemplate restTemplate;
    private final EnvironmentProperties environmentProperties;
    private final Converter<ValidationResult, Message> messageToErrorResConverter;
    protected final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

    @Override
    public void notifyDsAboutError(Erro message) {
        info("responseHandle return 'Erro', DS will be notified", message);

        ResponseEntity<Message> response = restTemplate.postForEntity(environmentProperties.getDsUrl(), message, Message.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            info("responseHandle return 'Erro', DS was notified", message);
        } else {
            info("responseHandle return 'Erro', DS was NOT notified", message);
        }
    }

    protected ResponseEntity<Message> processRequest(Message requestMessage) {
        info("Request to DS", requestMessage);

        ResponseEntity<Message> responseMessageEntity = restTemplate.postForEntity(environmentProperties.getDsUrl(), requestMessage, Message.class);

        Message responseMessage = responseMessageEntity.getBody();

        info("Response from DS", responseMessage);

        responseMessage.setRequestMessage(requestMessage);

        return responseMessageEntity;
    }

    protected Message getMessage(RestClientException ex, Message request, ErrorCode errorCode) {
        warn("Cant receive response from DS", ex);
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

    protected abstract void info(String message, Message data);

    protected abstract void warn(String message, Throwable ex);

}
