package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.converter.rbkmoneyplatform.*;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.utils.CReqEncoder;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class ConverterConfig {

    @Bean
    public Converter<ValidationResult, Message> aResToRBKMoneyAuthenticationResponseConverter(
            ChallengeFlowTransactionInfoStorageService transactionInfoStorageService,
            DsProviderHolder dsProviderHolder) {
        return new AResToRBKMoneyAuthenticationResponseConverter(transactionInfoStorageService, dsProviderHolder);
    }

    @Bean
    public Converter<ValidationResult, Message> pResToRBKMoneyPreparationResponseConverter(
            DsProviderHolder dsProviderHolder,
            CardRangeConverter cardRangeConverter) {
        return new PResToRBKMoneyPreparationResponseConverter(dsProviderHolder, cardRangeConverter);
    }

    @Bean
    public Converter<ValidationResult, Message> rbkMoneyAuthenticationRequestToAReqConverter(
            DsProviderHolder dsProviderHolder,
            IdGenerator idGenerator) {
        return new RBKMoneyAuthenticationRequestToAReqConverter(dsProviderHolder, idGenerator);
    }

    @Bean
    public Converter<ValidationResult, Message> rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter(CReqEncoder cReqEncoder) {
        return new RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter(cReqEncoder);
    }

    @Bean
    public Converter<ValidationResult, Message> rbkMoneyPreparationRequestToPReqConverter(
            DsProviderHolder dsProviderHolder,
            IdGenerator idGenerator) {
        return new RBKMoneyPreparationRequestToPReqConverter(dsProviderHolder, idGenerator);
    }
}
