package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.damsel.schedule.SchedulatorSrv;
import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowDsProviderProperties;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowScheduleProperties;
import com.rbkmoney.threeds.server.serializer.ThriftSerializer;
import com.rbkmoney.threeds.server.service.schedule.RBKMoneyPreparationFlowScheduler;
import com.rbkmoney.threeds.server.service.schedule.SchedulatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class PreparationFlowScheduleConfig {

    @Bean
    public RBKMoneyPreparationFlowScheduler rbkMoneyPreparationFlowScheduler(
            PreparationFlowDsProviderProperties visaPreparationFlowProperties,
            PreparationFlowDsProviderProperties mastercardPreparationFlowProperties,
            PreparationFlowDsProviderProperties mirPreparationFlowProperties,
            SchedulatorService schedulatorService) {
        return new RBKMoneyPreparationFlowScheduler(
                visaPreparationFlowProperties,
                mastercardPreparationFlowProperties,
                mirPreparationFlowProperties,
                schedulatorService);
    }

    @Bean
    public SchedulatorService schedulatorService(
            @Value("${client.three-ds-server-storage.card-ranges.url}") String threeDsServerStorageUrl,
            PreparationFlowScheduleProperties preparationFlowScheduleProperties,
            ThriftSerializer<InitRBKMoneyPreparationFlowRequest> preparationFlowRequestThriftSerializer,
            SchedulatorSrv.Iface schedulatorClient) {
        return new SchedulatorService(
                threeDsServerStorageUrl,
                preparationFlowScheduleProperties,
                preparationFlowRequestThriftSerializer,
                schedulatorClient);

    }

    @Bean
    public ThriftSerializer<InitRBKMoneyPreparationFlowRequest> preparationFlowRequestSerializer() {
        return new ThriftSerializer<>();
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
