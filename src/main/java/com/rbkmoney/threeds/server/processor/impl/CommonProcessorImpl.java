package com.rbkmoney.threeds.server.processor.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.exeption.UnexpectedProcessorResultException;
import com.rbkmoney.threeds.server.processor.Processor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class CommonProcessorImpl implements Processor<ValidationResult, Message> {

    private final Processor<ValidationResult, Message> nextProcessor;
    private final Predicate<ValidationResult> forConvert;
    private final Converter<ValidationResult, Message> converter;

    @Override
    public Message process(ValidationResult validationResult) {
        if (forConvert.test(validationResult)) {
            return converter.convert(validationResult);
        } else if (nextProcessor != null) {
            return nextProcessor.process(validationResult);
        }

        throw new UnexpectedProcessorResultException();
    }
}
