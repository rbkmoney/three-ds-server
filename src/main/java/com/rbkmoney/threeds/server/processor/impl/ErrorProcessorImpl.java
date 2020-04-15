package com.rbkmoney.threeds.server.processor.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.core.convert.converter.Converter;

import static java.util.function.Predicate.not;

public class ErrorProcessorImpl extends CommonProcessorImpl {

    public ErrorProcessorImpl(Processor<ValidationResult, Message> nextProcessor,
                              Converter<ValidationResult, Message> converter) {
        super(nextProcessor, not(ValidationResult::isValid), converter);
    }
}
