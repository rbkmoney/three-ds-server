package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.converter.PGcqToPGcsConverter;
import com.rbkmoney.threeds.server.converter.PPrqToPReqConverter;
import com.rbkmoney.threeds.server.converter.PResToPPrsConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.rbkmoney.threeds.server.config.builder.ProcessorBuilder.createProcessorChain;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class ProcessorConfig {

    @Bean
    public Processor<ValidationResult, Message> pGcqToPGcsProcessorChain(
            PGcqToPGcsConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pPrqToPReqProcessorChain(
            PPrqToPReqConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }

    @Bean
    public Processor<ValidationResult, Message> pResToPPrsProcessorChain(
            PResToPPrsConverter converter,
            MessageToErrorResConverter errorConverter) {
        return createProcessorChain(converter, errorConverter);
    }
}
