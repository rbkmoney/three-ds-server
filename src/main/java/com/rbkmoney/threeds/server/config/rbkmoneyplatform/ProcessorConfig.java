package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.config.builder.ProcessorBuilder.createProcessorChain;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class ProcessorConfig {

    @Bean
    public Processor<ValidationResult, Message> aResToRBKMoneyAuthenticationResponseProcessorChain(
            Converter<ValidationResult, Message> aResToRBKMoneyAuthenticationResponseConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(aResToRBKMoneyAuthenticationResponseConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pResToRBKMoneyPreparationResponseProcessorChain(
            Converter<ValidationResult, Message> pResToRBKMoneyPreparationResponseConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(pResToRBKMoneyPreparationResponseConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> rBKMoneyAuthenticationRequestToAReqProcessorChain(
            Converter<ValidationResult, Message> rBKMoneyAuthenticationRequestToAReqConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(rBKMoneyAuthenticationRequestToAReqConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseProcessorChain(
            Converter<ValidationResult, Message> rBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(rBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> rbkMoneyPreparationRequestToPReqProcessorChain(
            Converter<ValidationResult, Message> rBKMoneyPreparationRequestToPReqConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(rBKMoneyPreparationRequestToPReqConverter, messageToErrorResConverter);
    }
}
