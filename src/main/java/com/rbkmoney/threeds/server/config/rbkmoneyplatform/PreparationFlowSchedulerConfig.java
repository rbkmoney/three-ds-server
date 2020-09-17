package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.PreparationFlowDsProviderProperties;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowScheduleProperties;
import com.rbkmoney.threeds.server.service.RBKMoneyPreparationFlowScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class PreparationFlowSchedulerConfig {

    @Bean
    public RBKMoneyPreparationFlowScheduler rbkMoneyPreparationFlowScheduler(
            @Value("${client.three-ds-server-storage.card-ranges.url}") String threeDsServerStorageUrl,
            PreparationFlowScheduleProperties preparationFlowScheduleProperties,
            PreparationFlowDsProviderProperties visaPreparationFlowProperties,
            PreparationFlowDsProviderProperties mastercardPreparationFlowProperties,
            PreparationFlowDsProviderProperties mirPreparationFlowProperties) {
        return new RBKMoneyPreparationFlowScheduler(
                threeDsServerStorageUrl,
                preparationFlowScheduleProperties,
                visaPreparationFlowProperties,
                mastercardPreparationFlowProperties,
                mirPreparationFlowProperties);
    }

    @Bean
    @ConfigurationProperties("preparation-flow.schedule")
    public PreparationFlowScheduleProperties preparationFlowScheduleProperties() {
        return new PreparationFlowScheduleProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.visa")
    public PreparationFlowDsProviderProperties visaPreparationFlowProperties() {
        return new PreparationFlowDsProviderProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.mastercard")
    public PreparationFlowDsProviderProperties mastercardPreparationFlowProperties() {
        return new PreparationFlowDsProviderProperties();
    }

    @Bean
    @ConfigurationProperties("preparation-flow.ds-provider.mir")
    public PreparationFlowDsProviderProperties mirPreparationFlowProperties() {
        return new PreparationFlowDsProviderProperties();
    }
}
