package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;

public class PResToRBKMoneyPreparationResponseHandlerImpl extends ResponseHandlerImpl {

    public PResToRBKMoneyPreparationResponseHandlerImpl(
            Processor<ValidationResult, Message> processor,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder) {
        super(processor, validator, providerHolder);
    }

    @Override
    public boolean canHandle(Message message) {
        return message instanceof PRes;
    }
}
