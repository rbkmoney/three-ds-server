package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.threeds.server.config.properties.PreparationProperties;
import com.rbkmoney.threeds.server.service.RBKMoneyPreparationFlowTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class PreparationFlowTaskServiceConfig {

    @Bean
    public RBKMoneyPreparationFlowTaskService rbkMoneyPreparationFlowTaskService(
            @Value("${preparation-flow.on-startup.enabled}") boolean isEnabledOnStartup,
            @Value("${preparation-flow.on-schedule.enabled}") boolean isEnabledOnSchedule,
            PreparationProperties visaPreparationProperties,
            PreparationProperties mastercardPreparationProperties,
            PreparationProperties mirPreparationProperties,
            CardRangesStorageSrv.Iface cardRangesStorage) {
        return new RBKMoneyPreparationFlowTaskService(isEnabledOnStartup, isEnabledOnSchedule, visaPreparationProperties,
                mastercardPreparationProperties, mirPreparationProperties, cardRangesStorage);
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.visa")
    public PreparationProperties visaPreparationProperties() {
        return new PreparationProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.mastercard")
    public PreparationProperties mastercardPreparationProperties() {
        return new PreparationProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.mir")
    public PreparationProperties mirPreparationProperties() {
        return new PreparationProperties();
    }
}
