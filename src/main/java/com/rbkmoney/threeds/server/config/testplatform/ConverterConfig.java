package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.converter.testplatform.AResToPArsConverter;
import com.rbkmoney.threeds.server.converter.testplatform.PArqToAReqConverter;
import com.rbkmoney.threeds.server.converter.testplatform.PGcqToPGcsConverter;
import com.rbkmoney.threeds.server.converter.testplatform.PPrqToPReqConverter;
import com.rbkmoney.threeds.server.converter.testplatform.PResToPPrsConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import com.rbkmoney.threeds.server.utils.Base64Encoder;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import com.rbkmoney.threeds.server.utils.TemplateBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class ConverterConfig {

    @Bean
    public Converter<ValidationResult, Message> aResToPArsConverter(
            TestPlatformChallengeFlowTransactionInfoStorageService testPlatformChallengeFlowTransactionInfoStorageService) {
        return new AResToPArsConverter(testPlatformChallengeFlowTransactionInfoStorageService);
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
            TemplateBuilder templateBuilder,
            Base64Encoder base64Encoder) {
        return new PGcqToPGcsConverter(testPlatformChallengeFlowTransactionInfoStorageService, templateBuilder,
                base64Encoder);
    }

    @Bean
    public Converter<ValidationResult, Message> pPrqToPReqConverter(
            EnvironmentProperties environmentProperties,
            TestPlatformSerialNumStorageService testPlatformSerialNumStorageService) {
        return new PPrqToPReqConverter(environmentProperties, testPlatformSerialNumStorageService);
    }

    @Bean
    public Converter<ValidationResult, Message> pResToPPrsConverter() {
        return new PResToPPrsConverter();
    }
}
