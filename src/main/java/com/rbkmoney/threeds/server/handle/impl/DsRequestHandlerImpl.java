package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class DsRequestHandlerImpl implements DsRequestHandler {

    private final Processor<ValidationResult, Message> processor;
    private final MessageValidatorService validator;

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = validator.validate(message);
        return processor.process(validationResult);
    }
}
