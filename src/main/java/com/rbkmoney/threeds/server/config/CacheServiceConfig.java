package com.rbkmoney.threeds.server.config;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.CardRangesConverter;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import com.rbkmoney.threeds.server.service.cache.InMemoryCacheService;
import com.rbkmoney.threeds.server.service.cache.ThreeDsServerStorageCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CacheServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "storage.mode", havingValue = "IN_MEMORY")
    public CacheService inMemoryCacheService(
            @Value("${storage.cache.challenge-flow-transaction-info.size}") long challengeFlowTransactionInfoCacheSize) {
        return new InMemoryCacheService(challengeFlowTransactionInfoCacheSize);
    }

    @Bean
    @ConditionalOnProperty(name = "storage.mode", havingValue = "EXTERNAL")
    public CacheService threeDsServerStorageCacheService(
            ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient,
            ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter,
            @Value("${storage.cache.challenge-flow-transaction-info.size}") long challengeFlowTransactionInfoCacheSize,
            CardRangesStorageSrv.Iface cardRangesStorageClient,
            CardRangesConverter cardRangesConverter,
            @Value("${storage.cache.card-ranges.expiration-hours}") long cardRangesCacheExpirationHours) {
        return new ThreeDsServerStorageCacheService(
                challengeFlowTransactionInfoStorageClient,
                challengeFlowTransactionInfoConverter,
                challengeFlowTransactionInfoCacheSize,
                cardRangesStorageClient,
                cardRangesConverter,
                cardRangesCacheExpirationHours);
    }
}
