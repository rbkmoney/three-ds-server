package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.rbkmoney.damsel.threeds.server.storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.damsel.threeds.server.storage.PreparationFlowInitializerSrv;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowDsProviderProperties;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.exception.ExternalStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyPreparationFlowCronScheduler {

    private final boolean isEnabledOnSchedule;
    private final PreparationFlowDsProviderProperties visaProperties;
    private final PreparationFlowDsProviderProperties mastercardProperties;
    private final PreparationFlowDsProviderProperties mirProperties;
    private final PreparationFlowInitializerSrv.Iface preparationFlowInitializerClient;

    @EventListener(value = ApplicationReadyEvent.class)
    public void onStartup() {
        updatePreparationFlow();
    }

    @Scheduled(cron = "${rbkmoney-preparation-flow.scheduler.schedule.cron}")
    public void onSchedule() {
        if (isEnabledOnSchedule) {
            updatePreparationFlow();
        }
    }

    private void updatePreparationFlow() {
        updateJob(
                visaProperties.isEnabled(),
                DsProvider.VISA.getId(),
                visaProperties.getMessageVersion());

        updateJob(
                mastercardProperties.isEnabled(),
                DsProvider.MASTERCARD.getId(),
                mastercardProperties.getMessageVersion());

        updateJob(
                mirProperties.isEnabled(),
                DsProvider.MIR.getId(),
                mirProperties.getMessageVersion());
    }

    private void updateJob(
            boolean isEnabled,
            String dsProviderId,
            String messageVersion) {
        if (isEnabled) {
            initRBKMoneyPreparationFlow(dsProviderId, messageVersion);
        }
    }

    private void initRBKMoneyPreparationFlow(String dsProviderId, String messageVersion) {
        try {
            log.info("Init RBKMoney preparation flow, dsProviderId={}", dsProviderId);

            InitRBKMoneyPreparationFlowRequest request = new InitRBKMoneyPreparationFlowRequest()
                    .setProviderId(dsProviderId)
                    .setMessageVersion(messageVersion);

            preparationFlowInitializerClient.initRBKMoneyPreparationFlow(request);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }
}
