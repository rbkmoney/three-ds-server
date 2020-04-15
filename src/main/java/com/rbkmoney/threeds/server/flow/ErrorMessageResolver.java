package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;

public interface ErrorMessageResolver {

    String resolveErrorDescription(String messageTemplate);

    String resolveDefaultErrorDescription(ErrorCode errorCode);

    String resolveDefaultErrorDetail(ErrorCode errorCode);

}
