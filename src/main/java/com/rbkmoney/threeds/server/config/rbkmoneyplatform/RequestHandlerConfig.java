package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router.RBKMoneyAuthenticationRequestDsProviderRouter;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router.RBKMoneyGetChallengeRequestDsProviderRouter;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router.RBKMoneyPreparationRequestDsProviderRouter;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createRequestHandlerWithRouting;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class RequestHandlerConfig {

    @Bean
    public RequestHandler rBKMoneyAuthenticationRequestToAReqHandler(
            DsProviderHolder dsProviderHolder,
            RBKMoneyAuthenticationRequestDsProviderRouter rBKMoneyAuthenticationRequestDsProviderRouter,
            Processor<ValidationResult, Message> rBKMoneyAuthenticationRequestToAReqProcessorChain,
            MessageValidatorService messageValidatorService) {
        return createRequestHandlerWithRouting(
                dsProviderHolder,
                rBKMoneyAuthenticationRequestDsProviderRouter,
                rBKMoneyAuthenticationRequestToAReqProcessorChain,
                messageValidatorService,
                message -> message instanceof RBKMoneyAuthenticationRequest);
    }

    @Bean
    public RequestHandler rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseHandler(
            DsProviderHolder dsProviderHolder,
            RBKMoneyGetChallengeRequestDsProviderRouter rbkMoneyGetChallengeRequestDsProviderRouter,
            Processor<ValidationResult, Message> rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseProcessorChain,
            MessageValidatorService messageValidatorService) {
        return createRequestHandlerWithRouting(
                dsProviderHolder,
                rbkMoneyGetChallengeRequestDsProviderRouter,
                rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseProcessorChain,
                messageValidatorService,
                message -> message instanceof RBKMoneyGetChallengeRequest);
    }

    @Bean
    public RequestHandler rbkMoneyPreparationRequestToPReqHandler(
            DsProviderHolder dsProviderHolder,
            RBKMoneyPreparationRequestDsProviderRouter rbkMoneyPreparationRequestDsProviderRouter,
            Processor<ValidationResult, Message> rbkMoneyPreparationRequestToPReqProcessorChain,
            MessageValidatorService messageValidatorService) {
        return createRequestHandlerWithRouting(
                dsProviderHolder,
                rbkMoneyPreparationRequestDsProviderRouter,
                rbkMoneyPreparationRequestToPReqProcessorChain,
                messageValidatorService,
                message -> message instanceof RBKMoneyPreparationRequest);
    }
}
