package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeRequestHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createRequestHandler;

@Configuration("commonRequestHandlerConfig")
public class RequestHandlerConfig {

    @Bean
    public RequestHandler pReqToFixedPReqHandler(
            Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain,
            MessageValidatorService messageValidatorService) {
        return createRequestHandler(pReqToFixedPReqProcessorChain, messageValidatorService, message -> message instanceof PReq);
    }

    @Bean
    public RequestHandler unsupportedMessageTypeRequestHandler(MessageToErrorResConverter errorConverter) {
        return new UnsupportedMessageTypeRequestHandler(errorConverter);
    }
}
