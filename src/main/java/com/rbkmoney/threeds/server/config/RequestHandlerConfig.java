package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.handle.impl.*;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.router.impl.PArqDirectoryServerRouter;
import com.rbkmoney.threeds.server.router.impl.RBKMoneyPreparationRequestDirectoryServerRouter;
import com.rbkmoney.threeds.server.router.impl.TestDirectoryServerRouter;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestHandlerConfig {

    @Bean
    public RequestHandler pArqToAReqHandler(
            DirectoryServerProviderHolder providerHolder,
            PArqDirectoryServerRouter pArqDirectoryServerRouter,
            Processor<ValidationResult, Message> pArqToAReqProcessorChain,
            MessageValidatorService validator) {
        return new PArqToAReqHandlerImpl(
                providerHolder,
                pArqDirectoryServerRouter,
                pArqToAReqProcessorChain,
                validator);
    }

    @Bean
    @ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "TEST_PLATFORM")
    public RequestHandler pGcqToPGcsHandler(
            DirectoryServerProviderHolder providerHolder,
            TestDirectoryServerRouter testDirectoryServerRouter,
            Processor<ValidationResult, Message> pGcqToPGcsProcessorChain,
            MessageValidatorService validator) {
        return new PGcqToPGcsHandlerImpl(
                providerHolder,
                testDirectoryServerRouter,
                pGcqToPGcsProcessorChain,
                validator);
    }

    @Bean
    public RequestHandler pReqToFixedPReqHandler(
            Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain,
            MessageValidatorService validator) {
        return new PReqToFixedPReqHandlerImpl(pReqToFixedPReqProcessorChain, validator);
    }

    @Bean
    @ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "TEST_PLATFORM")
    public RequestHandler pPrqToPReqHandler(
            DirectoryServerProviderHolder providerHolder,
            TestDirectoryServerRouter testDirectoryServerRouter,
            Processor<ValidationResult, Message> pPrqToPReqProcessorChain,
            MessageValidatorService validator) {
        return new PPrqToPReqHandlerImpl(
                providerHolder,
                testDirectoryServerRouter,
                pPrqToPReqProcessorChain,
                validator);
    }

    @Bean
    @ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "RBK_MONEY")
    public RequestHandler rbkMoneyPreparationRequestToPReqHandler(
            DirectoryServerProviderHolder providerHolder,
            RBKMoneyPreparationRequestDirectoryServerRouter rbkMoneyPreparationRequestDirectoryServerRouter,
            Processor<ValidationResult, Message> rbkMoneyPreparationRequestToPReqProcessorChain,
            MessageValidatorService validator) {
        return new RBKMoneyPreparationRequestToPReqHandlerImpl(
                providerHolder,
                rbkMoneyPreparationRequestDirectoryServerRouter,
                rbkMoneyPreparationRequestToPReqProcessorChain,
                validator);
    }

    @Bean
    public RequestHandler unsupportedMessageTypeRequestHandler(MessageToErrorResConverter errorConverter) {
        return new UnsupportedMessageTypeRequestHandlerImpl(errorConverter);
    }
}
