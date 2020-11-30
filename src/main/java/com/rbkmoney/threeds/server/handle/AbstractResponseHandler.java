package com.rbkmoney.threeds.server.handle;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractResponseHandler implements ResponseHandler {

    private final Processor<ValidationResult, Message> processor;
    private final MessageValidatorService messageValidatorService;
    private final DsProviderHolder dsProviderHolder;

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = messageValidatorService.validate(message);
        Message result = processor.process(validationResult);
        handleErrorMessage(result);
        return result;
    }

    private void handleErrorMessage(Message result) {
        if (result instanceof Erro && ((Erro) result).isNotifyDsAboutError()) {
            dsProviderHolder.getDsClient().notifyDsAboutError((Erro) result);
        }
    }
}
