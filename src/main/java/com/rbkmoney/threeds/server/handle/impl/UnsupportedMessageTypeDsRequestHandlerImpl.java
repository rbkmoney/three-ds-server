package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.rbkmoney.threeds.server.constants.MessageConstants.INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT;
import static com.rbkmoney.threeds.server.constants.MessageConstants.UNSUPPORTED_MESSAGE_TYPE;

@RequiredArgsConstructor
@Slf4j
public class UnsupportedMessageTypeDsRequestHandlerImpl implements DsRequestHandler {

    private final MessageToErrorResConverter errorConverter;

    @Override
    public boolean canHandle(Message o) {
        return false;
    }

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = createFailure(message);
        return errorConverter.convert(validationResult);
    }

    private ValidationResult createFailure(Message message) {
        return ValidationResult.failure(
                ErrorCode.MESSAGE_RECEIVED_INVALID_101,
                INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT,
                UNSUPPORTED_MESSAGE_TYPE,
                message
        );
    }
}
