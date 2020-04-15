package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

public interface ErrorCodeResolver {

    ErrorCode resolve(String messageTemplate);

    ErrorCode resolve(ResourceAccessException ex);

    ErrorCode resolve(RestClientException ex);

    ErrorCode resolve(HttpMessageNotReadableException ex);

    ErrorCode resolve(HttpRequestMethodNotSupportedException ex);

}
