package com.rbkmoney.threeds.server.config;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class ThreeDsServerStorageConfig {

    @Bean
    public CardRangesStorageSrv.Iface cardRangesStorageClient(
            @Value("${client.three-ds-server-storage.card-ranges.url}") Resource url,
            @Value("${client.three-ds-server-storage.card-ranges.timeout}") int timeout) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(url.getURI())
                .withNetworkTimeout(timeout)
                .build(CardRangesStorageSrv.Iface.class);
    }

    @Bean
    public ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient(
            @Value("${client.three-ds-server-storage.challenge-flow-transaction-info.url}") Resource url,
            @Value("${client.three-ds-server-storage.challenge-flow-transaction-info.timeout}") int timeout) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(url.getURI())
                .withNetworkTimeout(timeout)
                .build(ChallengeFlowTransactionInfoStorageSrv.Iface.class);
    }
}
