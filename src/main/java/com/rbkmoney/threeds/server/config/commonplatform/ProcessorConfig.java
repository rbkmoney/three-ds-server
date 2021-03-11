package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.config.builder.ProcessorBuilder.createProcessorChain;

@Configuration("commonProcessorConfig")
@SuppressWarnings({"checkstyle:parametername", "checkstyle:methodname"})
public class ProcessorConfig {

    @Bean
    public Processor<ValidationResult, Message> dummyProcessorChain(
            Converter<ValidationResult, Message> dummyConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(dummyConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> erroWrapperToErroProcessorChain(
            Converter<ValidationResult, Message> erroWrapperToErroConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(erroWrapperToErroConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pReqToFixedPReqProcessorChain(
            Converter<ValidationResult, Message> pReqToFixedPReqConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(pReqToFixedPReqConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> rReqToRResProcessorChain(
            Converter<ValidationResult, Message> rReqToRResConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(rReqToRResConverter, messageToErrorResConverter);
    }
}
