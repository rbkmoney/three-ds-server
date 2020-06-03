package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeResponseHandler;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createResponseHandler;

@Configuration
@ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "TEST_PLATFORM")
public class ResponseHandlerConfig {

    @Bean
    public ResponseHandler aResToPArsHandler(
            Processor<ValidationResult, Message> aResToPArsProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder directoryServerProviderHolder) {
        return createResponseHandler(
                aResToPArsProcessorChain,
                validator,
                directoryServerProviderHolder,
                message -> message instanceof ARes);
    }

    @Bean
    public ResponseHandler pResToPPrsHandler(
            Processor<ValidationResult, Message> pResToPPrsProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder directoryServerProviderHolder) {
        return createResponseHandler(
                pResToPPrsProcessorChain,
                validator,
                directoryServerProviderHolder,
                message -> message instanceof PRes);
    }

    @Bean
    public ResponseHandler errorResDummyHandler(
            Processor<ValidationResult, Message> dummyProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder directoryServerProviderHolder) {
        return createResponseHandler(
                dummyProcessorChain,
                validator,
                directoryServerProviderHolder,
                message -> message instanceof Erro);
    }

    @Bean
    public ResponseHandler erroWrapperToErroResponseHandler(
            Processor<ValidationResult, Message> erroWrapperToErroProcessorChain,
            MessageValidatorService validator,
            DirectoryServerProviderHolder directoryServerProviderTestPlatformHolder) {
        return createResponseHandler(
                erroWrapperToErroProcessorChain,
                validator,
                directoryServerProviderTestPlatformHolder,
                message -> message instanceof ErroWrapper);
    }

    @Bean
    public ResponseHandler unsupportedMessageTypeResponseHandler(
            MessageToErrorResConverter errorConverter,
            DirectoryServerProviderHolder directoryServerProviderHolder) {
        return new UnsupportedMessageTypeResponseHandler(errorConverter, directoryServerProviderHolder);
    }
}
