package com.rbkmoney.threeds.server.config;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.CardRangesConverter;
import com.rbkmoney.threeds.server.converter.RReqTransactionInfoConverter;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import com.rbkmoney.threeds.server.service.cache.InMemoryCacheService;
import com.rbkmoney.threeds.server.service.cache.ThreeDsServerStorageCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CacheServiceConfig {

    @Bean
    public CacheService inMemoryCacheService(
            @Value("${storage.cache.rreq-transaction-info.size}") long rReqTransactionInfoCacheSize) {
        return new InMemoryCacheService(rReqTransactionInfoCacheSize);
    }

    @Bean
    public CacheService threeDsServerStorageCacheService(
            RReqTransactionInfoStorageSrv.Iface rReqTransactionInfoStorageClient,
            RReqTransactionInfoConverter rReqTransactionInfoConverter,
            @Value("${storage.cache.rreq-transaction-info.size}") long rReqTransactionInfoCacheSize,
            CardRangesStorageSrv.Iface cardRangesStorageClient,
            CardRangesConverter cardRangesConverter,
            @Value("${storage.cache.card-ranges.expiration-hours}") long cardRangesCacheExpirationHours) {
        return new ThreeDsServerStorageCacheService(
                rReqTransactionInfoStorageClient,
                rReqTransactionInfoConverter,
                rReqTransactionInfoCacheSize,
                cardRangesStorageClient,
                cardRangesConverter,
                cardRangesCacheExpirationHours);
    }

    @Bean
    public CacheService configurableCacheService(
            @Value("${storage.in-memory-only}") boolean isInMemoryOnlyStorage,
            CacheService inMemoryCacheService,
            CacheService threeDsServerStorageCacheService) {
        if (isInMemoryOnlyStorage) {
            log.warn("Property 'storage.in-memory-only' is set to TRUE. " +
                    "Consider switching it to FALSE if you're seeing this message on production environment");
            return inMemoryCacheService;
        }

        return threeDsServerStorageCacheService;
    }
}
