package com.rbkmoney.threeds.server.handle;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractResponseHandler implements ResponseHandler {

    private final Processor<ValidationResult, Message> processor;
    private final MessageValidatorService validator;
    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = validator.validate(message);
        Message result = processor.process(validationResult);
        handleErrorMessage(result);
        return result;
    }

    private void handleErrorMessage(Message result) {
        if (result instanceof Erro && ((Erro) result).isNotifyDsAboutError()) {
            providerHolder.getDsClient().notifyDsAboutError((Erro) result);
        }
    }
}
