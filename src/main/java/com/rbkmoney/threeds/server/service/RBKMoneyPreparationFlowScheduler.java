package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.config.properties.PreparationProperties;
import com.rbkmoney.threeds.server.ds.DsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class RBKMoneyPreparationFlowScheduler {

    private final boolean isEnabledOnSchedule;
    private final PreparationProperties visaPreparationProperties;
    private final PreparationProperties mastercardPreparationProperties;
    private final PreparationProperties mirPreparationProperties;

    @EventListener(value = ApplicationReadyEvent.class)
    public void onStartup() {
        if (isEnabledOnSchedule) {
            scheduleRBKMoneyPreparationFlow();
        } else {
            deregisterScheduledJobsIfExist();
        }
    }

    private void scheduleRBKMoneyPreparationFlow() {
        if (visaPreparationProperties.isEnabled()) {
            scheduleRBKMoneyPreparationFlow(DsProvider.VISA.getId(), visaPreparationProperties.getMessageVersion());
        }

        if (mastercardPreparationProperties.isEnabled()) {
            scheduleRBKMoneyPreparationFlow(DsProvider.MASTERCARD.getId(), mastercardPreparationProperties.getMessageVersion());
        }

        if (mirPreparationProperties.isEnabled()) {
            scheduleRBKMoneyPreparationFlow(DsProvider.MIR.getId(), mirPreparationProperties.getMessageVersion());
        }
    }

    private void scheduleRBKMoneyPreparationFlow(String dsProviderId, String messageVersion) {
            InitRBKMoneyPreparationFlowRequest initRBKMoneyPreparationFlowRequest = new InitRBKMoneyPreparationFlowRequest()
                    .setProviderId(dsProviderId)
                    .setMessageVersion(messageVersion);
    }

    private void deregisterScheduledJobsIfExist() {

    }
}
