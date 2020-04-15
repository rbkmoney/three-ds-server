package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Set;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ClientExceptionController extends ResponseEntityExceptionHandler {

    private final MessageToErrorResConverter messageToErrorConverter;
    private final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.warn("Request not readable", ex);

        Message message = getMessage(errorCodeResolver.resolve(ex));

        return handleExceptionInternal(ex, message, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        log.warn("Request method not supported", ex);

        Message message = getMessage(errorCodeResolver.resolve(ex));

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }

        return handleExceptionInternal(ex, message, headers, status, request);
    }

    private Message getMessage(ErrorCode errorCode) {
        return getMessage(errorCode, errorMessageResolver.resolveDefaultErrorDetail(errorCode), errorMessageResolver.resolveDefaultErrorDescription(errorCode));
    }

    private Message getMessage(ErrorCode errorCode, String errorDetail, String errorDescription) {
        return messageToErrorConverter.convert(ValidationResult.failure(errorCode, errorDetail, errorDescription, null));
    }
}
