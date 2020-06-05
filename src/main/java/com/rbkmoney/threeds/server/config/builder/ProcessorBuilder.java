package com.rbkmoney.threeds.server.config.builder;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.processor.impl.ErrorProcessorImpl;
import com.rbkmoney.threeds.server.processor.impl.ValidProcessorImpl;
import org.springframework.core.convert.converter.Converter;

public class ProcessorBuilder {

    public static Processor<ValidationResult, Message> createProcessorChain(
            Converter<ValidationResult, Message> converter,
            MessageToErrorResConverter errorConverter) {
        Processor<ValidationResult, Message> validProcessor = new ValidProcessorImpl(null, converter);
        return new ErrorProcessorImpl(validProcessor, errorConverter);
    }
}
