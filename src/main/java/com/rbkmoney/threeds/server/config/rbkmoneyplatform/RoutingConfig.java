package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router.RBKMoneyAuthenticationRequestDsProviderRouter;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router.RBKMoneyGetChallengeRequestDsProviderRouter;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router.RBKMoneyPreparationRequestDsProviderRouter;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class RoutingConfig {

    @Bean
    public RBKMoneyAuthenticationRequestDsProviderRouter rbkMoneyAuthenticationRequestDsProviderRouter(
            RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService) {
        return new RBKMoneyAuthenticationRequestDsProviderRouter(rbkMoneyCardRangesStorageService);
    }

    @Bean
    public RBKMoneyPreparationRequestDsProviderRouter rbkMoneyPreparationRequestDsProviderRouter() {
        return new RBKMoneyPreparationRequestDsProviderRouter();
    }

    @Bean
    public RBKMoneyGetChallengeRequestDsProviderRouter rbkMoneyGetChallengeRequestDsProviderRouter(
            RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService) {
        return new RBKMoneyGetChallengeRequestDsProviderRouter(rbkMoneyChallengeFlowTransactionInfoStorageService);
    }
}
