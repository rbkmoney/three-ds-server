package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.damsel.threeds.server.storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeMapper;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class StorageServiceConfig {

    @Bean
    public ChallengeFlowTransactionInfoStorageService challengeFlowTransactionInfoStorageService(
            RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService) {
        return rbkMoneyChallengeFlowTransactionInfoStorageService;
    }

    @Bean
    public RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService(
            ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient,
            ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter,
            @Value("${storage.challenge-flow-transaction-info.size}") long challengeFlowTransactionInfoCacheSize) {
        return new RBKMoneyChallengeFlowTransactionInfoStorageService(
                challengeFlowTransactionInfoStorageClient,
                challengeFlowTransactionInfoConverter,
                challengeFlowTransactionInfoCacheSize);
    }

    @Bean
    public RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService(
            CardRangesStorageSrv.Iface cardRangesStorageClient,
            CardRangeMapper cardRangeMapper) {
        return new RBKMoneyCardRangesStorageService(cardRangesStorageClient, cardRangeMapper);
    }
}
