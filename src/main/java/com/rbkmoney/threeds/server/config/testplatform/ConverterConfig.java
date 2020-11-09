package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.converter.testplatform.*;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
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
            ChallengeFlowTransactionInfoStorageService transactionInfoStorageService,
            EnvironmentMessageProperties messageProperties,
            DsProviderHolder dsProviderHolder) {
        return new AResToPArsConverter(transactionInfoStorageService, messageProperties, dsProviderHolder);
    }

    @Bean
    public Converter<ValidationResult, Message> pArqToAReqConverter(
            DsProviderHolder dsProviderHolder,
            IdGenerator idGenerator) {
        return new PArqToAReqConverter(dsProviderHolder, idGenerator);
    }

    @Bean
    public Converter<ValidationResult, Message> pGcqToPGcsConverter(
            ChallengeFlowTransactionInfoStorageService transactionInfoStorageService,
            VelocityEngine templateEngine,
            CReqEncoder cReqEncoder) {
        return new PGcqToPGcsConverter(transactionInfoStorageService, templateEngine, cReqEncoder);
    }

    @Bean
    public Converter<ValidationResult, Message> pPrqToPReqConverter(
            DsProviderHolder dsProviderHolder,
            TestPlatformSerialNumStorageService serialNumStorageService) {
        return new PPrqToPReqConverter(dsProviderHolder, serialNumStorageService);
    }

    @Bean
    public Converter<ValidationResult, Message> pResToPPrsConverter(
            EnvironmentMessageProperties messageProperties,
            TestPlatformSerialNumStorageService serialNumStorageService,
            CardRangesStorageService cardRangesStorageService) {
        return new PResToPPrsConverter(messageProperties, serialNumStorageService, (TestPlatformCardRangesStorageService) cardRangesStorageService);
    }
}
