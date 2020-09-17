package com.rbkmoney.threeds.server.config;

import com.rbkmoney.damsel.schedule.SchedulatorSrv;
import com.rbkmoney.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class SchedulatorConfig {

    @Bean
    public SchedulatorSrv.Iface schedulatorClient(
            @Value("${client.schedulator.url}") Resource url,
            @Value("${client.schedulator.timeout}") int timeout) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(url.getURI())
                .withNetworkTimeout(timeout)
                .build(SchedulatorSrv.Iface.class);
    }
}