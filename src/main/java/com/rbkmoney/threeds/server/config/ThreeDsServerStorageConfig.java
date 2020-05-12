package com.rbkmoney.threeds.server.config;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfoStorageSrv;
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
            @Value("${client.three-ds-server-storage.url}") Resource url,
            @Value("${client.three-ds-server-storage.timeout}") int timeout) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(url.getURI())
                .withNetworkTimeout(timeout)
                .build(CardRangesStorageSrv.Iface.class);
    }

    @Bean
    public RReqTransactionInfoStorageSrv.Iface rReqTransactionInfoStorageClient(
            @Value("${client.three-ds-server-storage.url}") Resource url,
            @Value("${client.three-ds-server-storage.timeout}") int timeout) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(url.getURI())
                .withNetworkTimeout(timeout)
                .build(RReqTransactionInfoStorageSrv.Iface.class);
    }
}