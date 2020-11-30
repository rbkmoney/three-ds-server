package com.rbkmoney.threeds.server.handle;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractRequestHandler implements RequestHandler, DsRequestHandler {

    private final Processor<ValidationResult, Message> processor;
    private final MessageValidatorService messageValidatorService;

    @Override
    public Message handle(Message message) {
        ValidationResult validationResult = messageValidatorService.validate(message);
        return processor.process(validationResult);
    }
}
