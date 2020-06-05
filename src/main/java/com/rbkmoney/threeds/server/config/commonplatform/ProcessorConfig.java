package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.converter.*;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.ProcessorBuilder.createProcessorChain;

@Configuration("commonProcessorConfig")
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
    public Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain(
            PReqToFixedPReqConverter converter,
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
}
