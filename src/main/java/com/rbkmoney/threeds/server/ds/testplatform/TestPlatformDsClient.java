package com.rbkmoney.threeds.server.ds.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.AbstractDsClient;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformLogWrapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class TestPlatformDsClient extends AbstractDsClient {

    private final TestPlatformLogWrapper testPlatformLogWrapper;

    public TestPlatformDsClient(RestTemplate restTemplate, EnvironmentProperties environmentProperties, Converter<ValidationResult, Message> messageToErrorResConverter, ErrorCodeResolver errorCodeResolver, ErrorMessageResolver errorMessageResolver, TestPlatformLogWrapper testPlatformLogWrapper) {
        super(restTemplate, environmentProperties, messageToErrorResConverter, errorCodeResolver, errorMessageResolver);
        this.testPlatformLogWrapper = testPlatformLogWrapper;
    }

    @Override
    public Message request(Message requestMessage) {
        try {
            ResponseEntity<Message> responseMessageEntity = processRequest(requestMessage);

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

    @Override
    protected void info(String message, Message data) {
        testPlatformLogWrapper.info(message, data);
    }

    @Override
    protected void warn(String message, Throwable ex) {
        testPlatformLogWrapper.warn(message, ex);
    }
}
