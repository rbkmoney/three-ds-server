package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.HandlerBuilder.createResponseHandler;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class ResponseHandlerConfig {

    @Bean
    public ResponseHandler pResToRBKMoneyPreparationResponseHandler(
            Processor<ValidationResult, Message> pResToRBKMoneyPreparationResponseProcessorChain,
            MessageValidatorService messageValidatorService,
            DsProviderHolder dsProviderHolder) {
        return createResponseHandler(
                pResToRBKMoneyPreparationResponseProcessorChain,
                messageValidatorService,
                dsProviderHolder,
                message -> message instanceof PRes);
    }
}
