package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeResponseHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createResponseHandler;

@Configuration("commonResponseHandlerConfig")
public class ResponseHandlerConfig {

    @Bean
    public ResponseHandler errorDummyResponseHandler(
            Processor<ValidationResult, Message> dummyProcessorChain,
            MessageValidatorService messageValidatorService,
            DsProviderHolder dsProviderHolder) {
        return createResponseHandler(
                dummyProcessorChain,
                messageValidatorService,
                dsProviderHolder,
                message -> message instanceof Erro);
    }

    @Bean
    public ResponseHandler erroWrapperToErroResponseHandler(
            Processor<ValidationResult, Message> erroWrapperToErroProcessorChain,
            MessageValidatorService messageValidatorService,
            DsProviderHolder dsProviderHolder) {
        return createResponseHandler(
                erroWrapperToErroProcessorChain,
                messageValidatorService,
                dsProviderHolder,
                message -> message instanceof ErroWrapper);
    }

    @Bean
    public ResponseHandler unsupportedMessageTypeResponseHandler(
            Converter<ValidationResult, Message> messageToErrorResConverter,
            DsProviderHolder dsProviderHolder) {
        return new UnsupportedMessageTypeResponseHandler(messageToErrorResConverter, dsProviderHolder);
    }
}
