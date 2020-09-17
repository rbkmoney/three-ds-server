package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.domain.BusinessScheduleRef;
import com.rbkmoney.damsel.domain.CalendarRef;
import com.rbkmoney.damsel.schedule.DominantBasedSchedule;
import com.rbkmoney.damsel.schedule.RegisterJobRequest;
import com.rbkmoney.damsel.schedule.Schedule;
import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowDsProviderProperties;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowScheduleProperties;
import com.rbkmoney.threeds.server.ds.DsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class RBKMoneyPreparationFlowScheduler {

    private final String threeDsServerStorageUrl;
    private final PreparationFlowScheduleProperties scheduleProperties;
    private final PreparationFlowDsProviderProperties visaProperties;
    private final PreparationFlowDsProviderProperties mastercardProperties;
    private final PreparationFlowDsProviderProperties mirProperties;

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
            registerJob(dsProviderId, messageVersion);
        } else {
            deregisterJob();
        }
    }

    private void registerJob(String dsProviderId, String messageVersion) {
        InitRBKMoneyPreparationFlowRequest initRBKMoneyPreparationFlowRequest = new InitRBKMoneyPreparationFlowRequest()
                .setProviderId(dsProviderId)
                .setMessageVersion(messageVersion);

        RegisterJobRequest registerJobRequest = new RegisterJobRequest()
                .setExecutorServicePath(threeDsServerStorageUrl)
                .setSchedule(Schedule.dominant_schedule(new DominantBasedSchedule()
                        .setBusinessScheduleRef(new BusinessScheduleRef()
                                .setId(scheduleProperties.getSchedulerId()))
                        .setCalendarRef(new CalendarRef()
                                .setId(scheduleProperties.getCalendarId()))
                        .setRevision(scheduleProperties.getRevisionId())))
                .setContext(new byte[0]); // TODO [a.romanov]: serialize preparationFlowRequest

        // TODO [a.romanov]: schedulator register
    }

    private void deregisterJob() {
        // TODO [a.romanov]: schedulator deregister
    }
}
