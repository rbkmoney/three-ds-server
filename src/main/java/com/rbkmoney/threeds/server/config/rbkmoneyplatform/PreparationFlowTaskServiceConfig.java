package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.PreparationProperties;
import com.rbkmoney.threeds.server.service.RBKMoneyPreparationFlowScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class PreparationFlowTaskServiceConfig {

    @Bean
    public RBKMoneyPreparationFlowScheduler rbkMoneyPreparationFlowScheduler(
            @Value("${preparation-flow.on-schedule.enabled}") boolean isEnabledOnSchedule,
            PreparationProperties visaPreparationProperties,
            PreparationProperties mastercardPreparationProperties,
            PreparationProperties mirPreparationProperties) {
        return new RBKMoneyPreparationFlowScheduler(
                isEnabledOnSchedule,
                visaPreparationProperties,
                mastercardPreparationProperties,
                mirPreparationProperties);
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
