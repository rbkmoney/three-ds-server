package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeConverter;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;
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
    public ChallengeFlowTransactionInfoStorageService transactionInfoStorageService(
            ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient,
            ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter,
            @Value("${storage.challenge-flow-transaction-info.size}") long challengeFlowTransactionInfoCacheSize) {
        return new RBKMoneyChallengeFlowTransactionInfoStorageService(challengeFlowTransactionInfoStorageClient, challengeFlowTransactionInfoConverter, challengeFlowTransactionInfoCacheSize);
    }

    @Bean
    public CardRangesStorageService cardRangesStorageService(
            CardRangesStorageSrv.Iface cardRangesStorageClient,
            CardRangeConverter cardRangeConverter) {
        return new RBKMoneyCardRangesStorageService(cardRangesStorageClient, cardRangeConverter);
    }
}
