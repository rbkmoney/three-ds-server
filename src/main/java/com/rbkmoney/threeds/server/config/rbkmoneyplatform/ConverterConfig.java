package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.converter.rbkmoneyplatform.AResToRBKMoneyAuthenticationResponseConverter;
import com.rbkmoney.threeds.server.converter.rbkmoneyplatform.PResToRBKMoneyPreparationResponseConverter;
import com.rbkmoney.threeds.server.converter.rbkmoneyplatform.RBKMoneyAuthenticationRequestToAReqConverter;
import com.rbkmoney.threeds.server.converter.rbkmoneyplatform.RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter;
import com.rbkmoney.threeds.server.converter.rbkmoneyplatform.RBKMoneyPreparationRequestToPReqConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.utils.Base64Encoder;
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
            RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService,
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder) {
        return new AResToRBKMoneyAuthenticationResponseConverter(rbkMoneyChallengeFlowTransactionInfoStorageService,
                rbkMoneyDsProviderHolder);
    }

    @Bean
    public Converter<ValidationResult, Message> pResToRBKMoneyPreparationResponseConverter(
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder) {
        return new PResToRBKMoneyPreparationResponseConverter(rbkMoneyDsProviderHolder);
    }

    @Bean
    public Converter<ValidationResult, Message> rbkMoneyAuthenticationRequestToAReqConverter(
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder) {
        return new RBKMoneyAuthenticationRequestToAReqConverter(rbkMoneyDsProviderHolder);
    }

    @Bean
    public Converter<ValidationResult, Message> rbkMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter(
            Base64Encoder base64Encoder) {
        return new RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter(base64Encoder);
    }

    @Bean
    public Converter<ValidationResult, Message> rbkMoneyPreparationRequestToPReqConverter(
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder,
            IdGenerator idGenerator) {
        return new RBKMoneyPreparationRequestToPReqConverter(rbkMoneyDsProviderHolder, idGenerator);
    }
}
