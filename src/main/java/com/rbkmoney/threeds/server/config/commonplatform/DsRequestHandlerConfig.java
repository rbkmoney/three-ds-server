package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeRequestHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createDsRequestHandler;

@Configuration("commonDsRequestHandlerConfig")
public class DsRequestHandlerConfig {

    @Bean
    public DsRequestHandler rReqToRResHandler(
            Processor<ValidationResult, Message> rReqToRResProcessorChain,
            MessageValidatorService messageValidatorService) {
        return createDsRequestHandler(rReqToRResProcessorChain, messageValidatorService, message -> message instanceof RReq);
    }

    @Bean
    public DsRequestHandler unsupportedMessageTypeDsRequestHandler(Converter<ValidationResult, Message> messageToErrorResConverter) {
        return new UnsupportedMessageTypeRequestHandler(messageToErrorResConverter);
    }
}
