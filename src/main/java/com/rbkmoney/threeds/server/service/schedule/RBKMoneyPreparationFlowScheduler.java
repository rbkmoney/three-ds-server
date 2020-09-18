package com.rbkmoney.threeds.server.service.schedule;

import com.rbkmoney.threeds.server.config.properties.PreparationFlowDsProviderProperties;
import com.rbkmoney.threeds.server.ds.DsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class RBKMoneyPreparationFlowScheduler {

    private final PreparationFlowDsProviderProperties visaProperties;
    private final PreparationFlowDsProviderProperties mastercardProperties;
    private final PreparationFlowDsProviderProperties mirProperties;
    private final SchedulatorService schedulatorService;

    @EventListener(value = ApplicationReadyEvent.class)
    public void onStartup() {
        updatePreparationFlowScheduledJobs();
    }

    private void updatePreparationFlowScheduledJobs() {
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
            schedulatorService.registerJob(dsProviderId, messageVersion);
        } else {
            schedulatorService.deregisterJob(dsProviderId);
        }
    }
}
