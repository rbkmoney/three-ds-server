package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeRequestHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createDsRequestHandler;

@Configuration("commonDsRequestHandlerConfig")
public class DsRequestHandlerConfig {

    @Bean
    public DsRequestHandler rReqToRResHandler(
            Processor<ValidationResult, Message> rReqToRResProcessorChain,
            MessageValidatorService validator) {
        return createDsRequestHandler(rReqToRResProcessorChain, validator, message -> message instanceof RReq);
    }

    @Bean
    public DsRequestHandler unsupportedMessageTypeDsRequestHandler(MessageToErrorResConverter errorConverter) {
        return new UnsupportedMessageTypeRequestHandler(errorConverter);
    }
}
