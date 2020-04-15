package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;

public class PGcqToPGcsHandlerImpl extends RequestHandlerImpl {

    public PGcqToPGcsHandlerImpl(Processor<ValidationResult, Message> processor, MessageValidatorService validator) {
        super(processor, validator);
    }

    @Override
    public boolean canHandle(Message message) {
        return message instanceof PGcq;
    }
}
