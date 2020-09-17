package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.domain.BusinessScheduleRef;
import com.rbkmoney.damsel.domain.CalendarRef;
import com.rbkmoney.damsel.schedule.*;
import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowDsProviderProperties;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowScheduleProperties;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.serializer.ThriftSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class RBKMoneyPreparationFlowScheduler {

    private final String threeDsServerStorageUrl;
    private final PreparationFlowScheduleProperties scheduleProperties;
    private final PreparationFlowDsProviderProperties visaProperties;
    private final PreparationFlowDsProviderProperties mastercardProperties;
    private final PreparationFlowDsProviderProperties mirProperties;
    private final ThriftSerializer<InitRBKMoneyPreparationFlowRequest> preparationFlowRequestSerializer;
    private final SchedulatorSrv.Iface schedulatorClient;

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
            log.info("Register schedulator job for DS provider with id={}", dsProviderId);
            registerJob(dsProviderId, messageVersion);
        } else {
            log.info("Deregister schedulator job for DS provider with id={}", dsProviderId);
            deregisterJob(dsProviderId);
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
                .setContext(preparationFlowRequestSerializer.serialize(initRBKMoneyPreparationFlowRequest));

        String jobId = scheduleProperties.getJobIdPrefix() + dsProviderId;

        try {
            schedulatorClient.registerJob(jobId, registerJobRequest);
        } catch (ScheduleAlreadyExists e) {
            log.info("Job with id={} already exists. No register needed", jobId, e);
        } catch (TException e) {
            log.error("Exception when trying to register job with id={}", jobId, e);
        }
    }

    private void deregisterJob(String dsProviderId) {
        String jobId = scheduleProperties.getJobIdPrefix() + dsProviderId;

        try {
            schedulatorClient.deregisterJob(jobId);
        } catch (ScheduleNotFound e) {
            log.info("Job with id={} does not exist. No deregister needed", jobId, e);
        } catch (TException e) {
            log.error("Exception when trying to deregister job with id={}", jobId, e);
        }
    }
}
