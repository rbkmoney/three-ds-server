package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.converter.PResToRBKMoneyPreparationResponseConverter;
import com.rbkmoney.threeds.server.converter.RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter;
import com.rbkmoney.threeds.server.converter.RBKMoneyPreparationRequestToPReqConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.ProcessorBuilder.createProcessorChain;

@Configuration
@ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "RBK_MONEY_PLATFORM")
public class ProcessorConfig {

    @Bean
    public Processor<ValidationResult, Message> rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseProcessorChain(
            RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> rbkMoneyPreparationRequestToPReqProcessorChain(
            RBKMoneyPreparationRequestToPReqConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pResToRBKMoneyPreparationResponseProcessorChain(
            PResToRBKMoneyPreparationResponseConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }
}
