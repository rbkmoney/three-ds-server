package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.rbkmoney.threeds.server.constants.MessageConstants.INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT;
import static com.rbkmoney.threeds.server.constants.MessageConstants.MESSAGE_IS_NOT_AREQ_ARES_CREQ_CRES_PREQ_PRES_RREQ_OR_RRES;

@RequiredArgsConstructor
@Slf4j
public class UnsupportedMessageTypeResponseHandlerImpl implements ResponseHandler {

    private final MessageToErrorResConverter errorConverter;
    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public boolean canHandle(Message o) {
        return false;
    }

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = getFailure(message);
        Message result = errorConverter.convert(validationResult);
        handleErrorMessage(result);
        return result;
    }

    private ValidationResult getFailure(Message message) {
        return ValidationResult.failure(
                ErrorCode.MESSAGE_RECEIVED_INVALID_101,
                INVALID_MESSAGE_FOR_THE_RECEIVING_COMPONENT,
                MESSAGE_IS_NOT_AREQ_ARES_CREQ_CRES_PREQ_PRES_RREQ_OR_RRES,
                message
        );
    }

    private void handleErrorMessage(Message result) {
        if (result instanceof Erro) {
            providerHolder.getDsClient().notifyDsAboutError((Erro) result);
        }
    }
}
