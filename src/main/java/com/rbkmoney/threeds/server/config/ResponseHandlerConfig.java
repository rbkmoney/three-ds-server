package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.handle.impl.*;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseHandlerConfig {

    @Bean
    public ResponseHandler errorResDummyHandler(Processor<ValidationResult, Message> dummyProcessorChain,
                                                MessageValidatorService validator,
                                                DsClient dsClient) {
        return new ErroDummyHandlerImpl(dummyProcessorChain, validator, dsClient);
    }

    @Bean
    public ResponseHandler erroWrapperToErroResponseHandler(Processor<ValidationResult, Message> erroWrapperToErroProcessorChain,
                                                            MessageValidatorService validator,
                                                            DsClient dsClient) {
        return new ErroWrapperToErroResponseHandlerImpl(erroWrapperToErroProcessorChain, validator, dsClient);
    }

    @Bean
    public ResponseHandler aResToPArsHandler(Processor<ValidationResult, Message> aResToPArsProcessorChain,
                                             MessageValidatorService validator,
                                             DsClient dsClient) {
        return new AResToPArsHandlerImpl(aResToPArsProcessorChain, validator, dsClient);
    }

    @Bean
    public ResponseHandler pResToPPrsHandler(Processor<ValidationResult, Message> pResToPPrsProcessorChain,
                                             MessageValidatorService validator,
                                             DsClient dsClient) {
        return new PResToPPrsHandlerImpl(pResToPPrsProcessorChain, validator, dsClient);
    }

    @Bean
    public ResponseHandler unsupportedMessageTypeResponseHandler(MessageToErrorResConverter errorConverter,
                                                                 DsClient dsClient) {
        return new UnsupportedMessageTypeResponseHandlerImpl(errorConverter, dsClient);
    }
}
