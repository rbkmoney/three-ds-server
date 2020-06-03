package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.handle.impl.UnsupportedMessageTypeRequestHandler;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.router.impl.PArqDirectoryServerRouter;
import com.rbkmoney.threeds.server.router.impl.RBKMoneyPreparationRequestDirectoryServerRouter;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createRequestHandler;
import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createRequestHandlerWithRouting;

@Configuration
@ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "RBK_MONEY_PLATFORM")
public class RequestHandlerConfig {

    @Bean
    public RequestHandler pArqToAReqHandler(
            DirectoryServerProviderHolder directoryServerProviderHolder,
            PArqDirectoryServerRouter pArqDirectoryServerRouter,
            Processor<ValidationResult, Message> pArqToAReqProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandlerWithRouting(
                directoryServerProviderHolder,
                pArqDirectoryServerRouter,
                pArqToAReqProcessorChain,
                validator,
                message -> message instanceof PArq);
    }

    @Bean
    public RequestHandler rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseHandler(
            Processor<ValidationResult, Message> rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandler(
                rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseProcessorChain,
                validator,
                message -> message instanceof RBKMoneyGetChallengeRequest);
    }

    @Bean
    public RequestHandler rbkMoneyPreparationRequestToPReqHandler(
            DirectoryServerProviderHolder directoryServerProviderHolder,
            RBKMoneyPreparationRequestDirectoryServerRouter rbkMoneyPreparationRequestDirectoryServerRouter,
            Processor<ValidationResult, Message> rbkMoneyPreparationRequestToPReqProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandlerWithRouting(
                directoryServerProviderHolder,
                rbkMoneyPreparationRequestDirectoryServerRouter,
                rbkMoneyPreparationRequestToPReqProcessorChain,
                validator,
                message -> message instanceof RBKMoneyPreparationRequest);
    }

    @Bean
    public RequestHandler pReqToFixedPReqHandler(
            Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain,
            MessageValidatorService validator) {
        return createRequestHandler(pReqToFixedPReqProcessorChain, validator, message -> message instanceof PReq);
    }

    @Bean
    public RequestHandler unsupportedMessageTypeRequestHandler(MessageToErrorResConverter errorConverter) {
        return new UnsupportedMessageTypeRequestHandler(errorConverter);
    }
}
