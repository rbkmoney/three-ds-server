package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.constants.MessageConstants.INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT;
import static com.rbkmoney.threeds.server.constants.MessageConstants.UNSUPPORTED_MESSAGE_TYPE;

@RequiredArgsConstructor
@Slf4j
public class UnsupportedMessageTypeResponseHandler implements ResponseHandler {

    private final Converter<ValidationResult, Message> messageToErrorResConverter;
    private final DsProviderHolder dsProviderHolder;

    @Override
    public boolean canHandle(Message o) {
        return false;
    }

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = failure(message);
        Message result = messageToErrorResConverter.convert(validationResult);
        dsProviderHolder.getDsClient().notifyDsAboutError((Erro) result);
        return result;
    }

    private ValidationResult failure(Message message) {
        return ValidationResult.failure(
                ErrorCode.MESSAGE_RECEIVED_INVALID_101,
                INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT,
                UNSUPPORTED_MESSAGE_TYPE,
                message
        );
    }
}
