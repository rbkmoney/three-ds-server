package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyLogWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class LogWrapperConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }

    @Bean
    public LogWrapper logWrapper(DsProviderHolder dsProviderHolder, ObjectMapper objectMapper, Gson gson) {
        return new RBKMoneyLogWrapper(dsProviderHolder, objectMapper, gson);
    }
}
