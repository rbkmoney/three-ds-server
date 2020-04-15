package com.rbkmoney.threeds.server.processor.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.core.convert.converter.Converter;

public class ValidProcessorImpl extends CommonProcessorImpl {

    public ValidProcessorImpl(Processor<ValidationResult, Message> nextProcessor,
                              Converter<ValidationResult, Message> converter) {
        super(nextProcessor, ValidationResult::isValid, converter);
    }
}
