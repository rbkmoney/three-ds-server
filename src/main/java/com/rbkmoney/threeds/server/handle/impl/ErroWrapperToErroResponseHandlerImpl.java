package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErroWrapperToErroResponseHandlerImpl extends ResponseHandlerImpl {

    public ErroWrapperToErroResponseHandlerImpl(Processor<ValidationResult, Message> processor, MessageValidatorService validator, DsClient dsClient) {
        super(processor, validator, dsClient);
    }

    @Override
    public boolean canHandle(Message message) {
        return message instanceof ErroWrapper;
    }
}
