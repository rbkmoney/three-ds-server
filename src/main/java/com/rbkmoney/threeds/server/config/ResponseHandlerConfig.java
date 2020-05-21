package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.handle.impl.*;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseHandlerConfig {

    @Bean
    public ResponseHandler errorResDummyHandler(
            Processor<ValidationResult, Message> dummyProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder) {
        return new ErroDummyHandlerImpl(dummyProcessorChain, validator, providerHolder);
    }

    @Bean
    public ResponseHandler erroWrapperToErroResponseHandler(
            Processor<ValidationResult, Message> erroWrapperToErroProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder) {
        return new ErroWrapperToErroResponseHandlerImpl(erroWrapperToErroProcessorChain, validator, providerHolder);
    }

    @Bean
    public ResponseHandler aResToPArsHandler(
            Processor<ValidationResult, Message> aResToPArsProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder) {
        return new AResToPArsHandlerImpl(aResToPArsProcessorChain, validator, providerHolder);
    }

    @Bean
    @ConditionalOnProperty(name = "preparation-flow", havingValue = "TEST_PLATFORM")
    public ResponseHandler pResToPPrsHandler(
            Processor<ValidationResult, Message> pResToPPrsProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder) {
        return new PResToPPrsHandlerImpl(pResToPPrsProcessorChain, validator, providerHolder);
    }

    @Bean
    @ConditionalOnProperty(name = "preparation-flow", havingValue = "RBK_MONEY")
    public ResponseHandler pResToRBKMoneyPreparationResponseHandler(
            Processor<ValidationResult, Message> pResToRBKMoneyPreparationResponseProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder) {
        return new PResToRBKMoneyPreparationResponseHandlerImpl(
                pResToRBKMoneyPreparationResponseProcessorChain,
                validator,
                providerHolder);
    }

    @Bean
    public ResponseHandler unsupportedMessageTypeResponseHandler(
            MessageToErrorResConverter errorConverter,
            DirectoryServerProviderHolder providerHolder) {
        return new UnsupportedMessageTypeResponseHandlerImpl(errorConverter, providerHolder);
    }
}
