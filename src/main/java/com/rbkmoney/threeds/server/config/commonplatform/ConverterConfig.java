package com.rbkmoney.threeds.server.config.commonplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.converter.commonplatform.DummyConverter;
import com.rbkmoney.threeds.server.converter.commonplatform.ErroWrapperToErroConverter;
import com.rbkmoney.threeds.server.converter.commonplatform.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.converter.commonplatform.PReqToFixedPReqConverter;
import com.rbkmoney.threeds.server.converter.commonplatform.RReqToRResConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration("commonConverterConfig")
public class ConverterConfig {

    @Bean
    public Converter<ValidationResult, Message> dummyConverter() {
        return new DummyConverter();
    }

    @Bean
    public Converter<ValidationResult, Message> erroWrapperToErroConverter() {
        return new ErroWrapperToErroConverter();
    }

    @Bean
    public Converter<ValidationResult, Message> messageToErrorResConverter(
            EnvironmentMessageProperties environmentMessageProperties) {
        return new MessageToErrorResConverter(environmentMessageProperties);
    }

    @Bean
    public Converter<ValidationResult, Message> pReqToFixedPReqConverter() {
        return new PReqToFixedPReqConverter();
    }

    @Bean
    public Converter<ValidationResult, Message> rReqToRResConverter() {
        return new RReqToRResConverter();
    }
}
