package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.Set;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ClientExceptionController extends ResponseEntityExceptionHandler {

    private final Converter<ValidationResult, Message> messageToErrorResConverter;
    private final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Message message = getMessage(errorCodeResolver.resolve(ex));

        return handleExceptionInternal(ex, message, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        Message message = getMessage(errorCodeResolver.resolve(ex));

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }

        return handleExceptionInternal(ex, message, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn(
                String.format("Some ServletException level error with handle servlet request, " +
                        "request=%s, response headers=%s, response status=%s", request.toString(), headers.toString(), status.toString()),
                ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowableExceptions(Throwable ex, WebRequest request) {
        log.error(
                String.format("Some internal error with handle servlet request, " +
                        "request=%s, handler will return INTERNAL_SERVER_ERROR", request.toString()),
                ex);
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Message getMessage(ErrorCode errorCode) {
        return getMessage(errorCode, errorMessageResolver.resolveDefaultErrorDetail(errorCode), errorMessageResolver.resolveDefaultErrorDescription(errorCode));
    }

    private Message getMessage(ErrorCode errorCode, String errorDetail, String errorDescription) {
        return messageToErrorResConverter.convert(ValidationResult.failure(errorCode, errorDetail, errorDescription, null));
    }
}
