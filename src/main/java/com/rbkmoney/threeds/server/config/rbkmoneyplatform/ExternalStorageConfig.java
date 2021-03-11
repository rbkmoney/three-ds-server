package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.damsel.threeds.server.storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class ExternalStorageConfig {

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
            @Value("${client.three-ds-server-storage.challenge-flow-transaction-info.timeout}") int timeout)
            throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(url.getURI())
                .withNetworkTimeout(timeout)
                .build(ChallengeFlowTransactionInfoStorageSrv.Iface.class);
    }
}
