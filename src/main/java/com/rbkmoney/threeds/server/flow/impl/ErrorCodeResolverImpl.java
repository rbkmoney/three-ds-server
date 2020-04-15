package com.rbkmoney.threeds.server.flow.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.dto.ConstraintType;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class ErrorCodeResolverImpl implements ErrorCodeResolver {

    @Override
    public ErrorCode resolve(String messageTemplate) {
        ConstraintType constraintType = ConstraintType.of(messageTemplate);
        switch (constraintType) {
            case OUT_OF_CARD_RANGE:
                return ErrorCode.MESSAGE_RECEIVED_INVALID_101;
            case NOT_BLANK:
            case NOT_NULL:
                return ErrorCode.REQUIRED_DATA_ELEMENT_MISSING_201;
            case CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED:
                return ErrorCode.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED_202;
            case PATTERN:
                return ErrorCode.FORMAT_OF_DATA_ELEMENTS_INVALID_203;
            case ID_NOT_RECOGNISED:
                return ErrorCode.TRANSACTION_ID_NOT_RECOGNISED_301;
            case AUTH_DEC_TIME_IS_EXPIRED:
                return ErrorCode.TRANSACTION_TIMED_OUT_402;
            default:
                throw new IllegalArgumentException("Unsupported annotation type: " + messageTemplate);
        }
    }

    @Override
    public ErrorCode resolve(ResourceAccessException ex) {
        ErrorCode errorCode;
        if (ex.getCause() instanceof SocketTimeoutException) {
            errorCode = ErrorCode.TRANSACTION_TIMED_OUT_402;
        } else {
            errorCode = ErrorCode.SYSTEM_CONNECTION_FAILURE_405;
        }
        return errorCode;
    }

    @Override
    public ErrorCode resolve(RestClientException ex) {
        ErrorCode errorCode;
        Throwable root = Optional.ofNullable(ex.getCause()).map(Throwable::getCause).orElse(null);
        if (root instanceof JsonParseException) {
            errorCode = ErrorCode.MESSAGE_RECEIVED_INVALID_101;
        } else if (root instanceof InvalidTypeIdException) {
            InvalidTypeIdException invalidTypeIdException = (InvalidTypeIdException) root;
            if (!isBlank(invalidTypeIdException.getTypeId())) {
                errorCode = ErrorCode.MESSAGE_RECEIVED_INVALID_101;
            } else {
                errorCode = ErrorCode.REQUIRED_DATA_ELEMENT_MISSING_201;
            }
        } else if (root instanceof InvalidFormatException) {
            errorCode = ErrorCode.FORMAT_OF_DATA_ELEMENTS_INVALID_203;
        } else if (root instanceof MismatchedInputException) {
            errorCode = ErrorCode.FORMAT_OF_DATA_ELEMENTS_INVALID_203;
        } else {
            throw ex;
        }
        return errorCode;
    }

    @Override
    public ErrorCode resolve(HttpMessageNotReadableException ex) {
        ErrorCode errorCode;
        Throwable root = Optional.ofNullable(ex.getCause()).orElse(null);
        if (root instanceof JsonParseException) {
            errorCode = ErrorCode.MESSAGE_RECEIVED_INVALID_101;
        } else if (root instanceof InvalidTypeIdException) {
            InvalidTypeIdException invalidTypeIdException = (InvalidTypeIdException) root;
            if (!isBlank(invalidTypeIdException.getTypeId())) {
                errorCode = ErrorCode.FORMAT_OF_DATA_ELEMENTS_INVALID_203;
            } else {
                errorCode = ErrorCode.REQUIRED_DATA_ELEMENT_MISSING_201;
            }
        } else if (root instanceof InvalidFormatException) {
            errorCode = ErrorCode.FORMAT_OF_DATA_ELEMENTS_INVALID_203;
        } else {
            throw ex;
        }
        return errorCode;
    }

    @Override
    public ErrorCode resolve(HttpRequestMethodNotSupportedException ex) {
        return ErrorCode.SYSTEM_CONNECTION_FAILURE_405;
    }
}
