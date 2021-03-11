package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.config.builder.ProcessorBuilder.createProcessorChain;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
@SuppressWarnings({"checkstyle:parametername", "checkstyle:methodname"})
public class ProcessorConfig {

    @Bean
    public Processor<ValidationResult, Message> aResToPArsProcessorChain(
            Converter<ValidationResult, Message> aResToPArsConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(aResToPArsConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pArqToAReqProcessorChain(
            Converter<ValidationResult, Message> pArqToAReqConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(pArqToAReqConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pGcqToPGcsProcessorChain(
            Converter<ValidationResult, Message> pGcqToPGcsConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(pGcqToPGcsConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pPrqToPReqProcessorChain(
            Converter<ValidationResult, Message> pPrqToPReqConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(pPrqToPReqConverter, messageToErrorResConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pResToPPrsProcessorChain(
            Converter<ValidationResult, Message> pResToPPrsConverter,
            Converter<ValidationResult, Message> messageToErrorResConverter) {
        return createProcessorChain(pResToPPrsConverter, messageToErrorResConverter);
    }
}
