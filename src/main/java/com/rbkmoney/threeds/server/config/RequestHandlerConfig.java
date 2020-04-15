package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.handle.impl.*;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestHandlerConfig {

    @Bean
    public RequestHandler erroWrapperToErroRequestHandler(Processor<ValidationResult, Message> erroWrapperToErroProcessorChain,
                                                          MessageValidatorService validator) {
        return new ErroWrapperToErroRequestHandlerImpl(erroWrapperToErroProcessorChain, validator);
    }

    @Bean
    public RequestHandler pArqToAReqHandler(Processor<ValidationResult, Message> pArqToAReqProcessorChain,
                                            MessageValidatorService validator) {
        return new PArqToAReqHandlerImpl(pArqToAReqProcessorChain, validator);
    }

    @Bean
    public RequestHandler pGcqToPGcsHandler(Processor<ValidationResult, Message> pGcqToPGcsProcessorChain,
                                            MessageValidatorService validator) {
        return new PGcqToPGcsHandlerImpl(pGcqToPGcsProcessorChain, validator);
    }

    @Bean
    public RequestHandler pPrqToPReqHandler(Processor<ValidationResult, Message> pPrqToPReqProcessorChain,
                                            MessageValidatorService validator) {
        return new PPrqToPReqHandlerImpl(pPrqToPReqProcessorChain, validator);
    }

    @Bean
    public RequestHandler pReqToFixedPReqHandler(Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain,
                                                 MessageValidatorService validator) {
        return new PReqToFixedPReqHandlerImpl(pReqToFixedPReqProcessorChain, validator);
    }

    @Bean
    public RequestHandler rReqToRResHandler(Processor<ValidationResult, Message> rReqToRResProcessorChain,
                                            MessageValidatorService validator) {
        return new RReqToRResHandlerImpl(rReqToRResProcessorChain, validator);
    }

    @Bean
    public RequestHandler unsupportedMessageTypeRequestHandler(MessageToErrorResConverter errorConverter,
                                                               DsClient dsClient) {
        return new UnsupportedMessageTypeRequestHandlerImpl(errorConverter, dsClient);
    }
}
