package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createResponseHandler;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
@SuppressWarnings({"checkstyle:parametername", "checkstyle:methodname"})
public class ResponseHandlerConfig {

    @Bean
    public ResponseHandler aResToPArsHandler(
            Processor<ValidationResult, Message> aResToPArsProcessorChain,
            MessageValidatorService messageValidatorService,
            DsProviderHolder dsProviderHolder) {
        return createResponseHandler(
                aResToPArsProcessorChain,
                messageValidatorService,
                dsProviderHolder,
                message -> message instanceof ARes);
    }

    @Bean
    public ResponseHandler pResToPPrsHandler(
            Processor<ValidationResult, Message> pResToPPrsProcessorChain,
            MessageValidatorService messageValidatorService,
            DsProviderHolder dsProviderHolder) {
        return createResponseHandler(
                pResToPPrsProcessorChain,
                messageValidatorService,
                dsProviderHolder,
                message -> message instanceof PRes);
    }
}
