package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import com.rbkmoney.threeds.server.service.log.RBKMoneyPlatformLogWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class LogWrapperConfig {

    @Bean
    public LogWrapper logWrapper(DsProviderHolder dsProviderHolder, ObjectMapper objectMapper, Gson gson) {
        return new RBKMoneyPlatformLogWrapper(dsProviderHolder, objectMapper, gson);
    }
}
