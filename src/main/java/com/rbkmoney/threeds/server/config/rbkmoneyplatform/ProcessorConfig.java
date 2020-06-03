package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.converter.*;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.processor.impl.ErrorProcessorImpl;
import com.rbkmoney.threeds.server.processor.impl.ValidProcessorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@ConditionalOnProperty(name = "preparation-flow.mode", havingValue = "RBK_MONEY_PLATFORM")
public class ProcessorConfig {

    @Bean
    public Processor<ValidationResult, Message> pArqToAReqProcessorChain(
            PArqToAReqConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> aResToPArsProcessorChain(
            AResToPArsConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

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
    public Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain(
            PReqToFixedPReqConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pResToRBKMoneyPreparationResponseProcessorChain(
            PResToRBKMoneyPreparationResponseConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> rReqToRResProcessorChain(
            RReqToRResConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> dummyProcessorChain(
            DummyConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> erroWrapperToErroProcessorChain(
            ErroWrapperToErroConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    private Processor<ValidationResult, Message> createProcessorChain(
            Converter<ValidationResult, Message> converter,
            MessageToErrorResConverter errorConverter) {
        Processor<ValidationResult, Message> validProcessor = new ValidProcessorImpl(null, converter);
        return new ErrorProcessorImpl(validProcessor, errorConverter);
    }
}
