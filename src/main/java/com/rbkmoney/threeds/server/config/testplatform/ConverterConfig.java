package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.testplatform.*;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import com.rbkmoney.threeds.server.utils.CReqEncoder;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class ConverterConfig {

    @Bean
    public Converter<ValidationResult, Message> aResToPArsConverter(
            TestPlatformChallengeFlowTransactionInfoStorageService testPlatformChallengeFlowTransactionInfoStorageService,
            EnvironmentMessageProperties environmentMessageProperties) {
        return new AResToPArsConverter(testPlatformChallengeFlowTransactionInfoStorageService, environmentMessageProperties);
    }

    @Bean
    public Converter<ValidationResult, Message> pArqToAReqConverter(
            EnvironmentProperties environmentProperties,
            IdGenerator idGenerator) {
        return new PArqToAReqConverter(environmentProperties, idGenerator);
    }

    @Bean
    public Converter<ValidationResult, Message> pGcqToPGcsConverter(
            TestPlatformChallengeFlowTransactionInfoStorageService testPlatformChallengeFlowTransactionInfoStorageService,
            VelocityEngine templateEngine,
            CReqEncoder cReqEncoder) {
        return new PGcqToPGcsConverter(testPlatformChallengeFlowTransactionInfoStorageService, templateEngine, cReqEncoder);
    }

    @Bean
    public Converter<ValidationResult, Message> pPrqToPReqConverter(
            EnvironmentProperties environmentProperties,
            TestPlatformSerialNumStorageService testPlatformSerialNumStorageService) {
        return new PPrqToPReqConverter(environmentProperties, testPlatformSerialNumStorageService);
    }

    @Bean
    public Converter<ValidationResult, Message> pResToPPrsConverter(
            EnvironmentMessageProperties environmentMessageProperties) {
        return new PResToPPrsConverter(environmentMessageProperties);
    }
}
