package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import com.rbkmoney.threeds.server.handle.impl.RReqToRResHandlerImpl;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeDsRequestHandlerImpl;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DsRequestHandlerConfig {

    @Bean
    public DsRequestHandler rReqToRResHandler(
            Processor<ValidationResult, Message> rReqToRResProcessorChain,
            MessageValidatorService validator) {
        return new RReqToRResHandlerImpl(rReqToRResProcessorChain, validator);
    }

    @Bean
    public DsRequestHandler unsupportedMessageTypeDsRequestHandler(MessageToErrorResConverter errorConverter) {
        return new UnsupportedMessageTypeDsRequestHandlerImpl(errorConverter);
    }
}
