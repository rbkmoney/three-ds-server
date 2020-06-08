package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createRequestHandler;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class RequestHandlerConfig {

    @Bean
    public RequestHandler pArqToAReqHandler(
            Processor<ValidationResult, Message> pArqToAReqProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandler(pArqToAReqProcessorChain, validator, message -> message instanceof PArq);
    }

    @Bean
    public RequestHandler pGcqToPGcsHandler(
            Processor<ValidationResult, Message> pGcqToPGcsProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandler(pGcqToPGcsProcessorChain, validator, message -> message instanceof PGcq);
    }

    @Bean
    public RequestHandler pPrqToPReqHandler(
            Processor<ValidationResult, Message> pPrqToPReqProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandler(pPrqToPReqProcessorChain, validator, message -> message instanceof PPrq);
    }
}
