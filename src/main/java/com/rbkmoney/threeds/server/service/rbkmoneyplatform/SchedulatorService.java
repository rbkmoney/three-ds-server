package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.rbkmoney.damsel.domain.BusinessScheduleRef;
import com.rbkmoney.damsel.domain.CalendarRef;
import com.rbkmoney.damsel.schedule.DominantBasedSchedule;
import com.rbkmoney.damsel.schedule.RegisterJobRequest;
import com.rbkmoney.damsel.schedule.SchedulatorSrv;
import com.rbkmoney.damsel.schedule.Schedule;
import com.rbkmoney.damsel.schedule.ScheduleAlreadyExists;
import com.rbkmoney.damsel.schedule.ScheduleNotFound;
import com.rbkmoney.damsel.threeds.server.storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.config.properties.PreparationFlowScheduleProperties;
import com.rbkmoney.threeds.server.serializer.ThriftSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

@Slf4j
@RequiredArgsConstructor
public class SchedulatorService {

    private final PreparationFlowScheduleProperties scheduleProperties;
    private final ThriftSerializer<InitRBKMoneyPreparationFlowRequest> preparationFlowRequestSerializer;
    private final SchedulatorSrv.Iface schedulatorClient;

    public void registerJob(String dsProviderId, String messageVersion) {
        String jobId = scheduleProperties.getJobIdPrefix() + dsProviderId;

        InitRBKMoneyPreparationFlowRequest initRBKMoneyPreparationFlowRequest = new InitRBKMoneyPreparationFlowRequest()
                .setProviderId(dsProviderId)
                .setMessageVersion(messageVersion);

        RegisterJobRequest registerJobRequest = new RegisterJobRequest()
                .setExecutorServicePath(scheduleProperties.getExecutorUrl())
                .setSchedule(Schedule.dominant_schedule(new DominantBasedSchedule()
                        .setBusinessScheduleRef(new BusinessScheduleRef()
                                .setId(scheduleProperties.getSchedulerId()))
                        .setCalendarRef(new CalendarRef()
                                .setId(scheduleProperties.getCalendarId()))
                        .setRevision(scheduleProperties.getRevisionId())))
                .setContext(preparationFlowRequestSerializer.serialize(initRBKMoneyPreparationFlowRequest));

        registerJobById(jobId, registerJobRequest);
    }

    public void deregisterJob(String dsProviderId) {
        String jobId = scheduleProperties.getJobIdPrefix() + dsProviderId;

        deregisterJobById(jobId);
    }

    private void registerJobById(String jobId, RegisterJobRequest registerJobRequest) {
        try {
            log.info("Register schedulator job , jobId={}", jobId);

            schedulatorClient.registerJob(jobId, registerJobRequest);
        } catch (ScheduleAlreadyExists e) {
            log.info("Job already exists. No register needed, jobId={}", jobId, e);
        } catch (TException e) {
            log.error("Exception when trying to register job, jobId={}", jobId, e);
        }
    }

    private void deregisterJobById(String jobId) {
        try {
            log.info("Deregister schedulator job, jobId={}", jobId);

            schedulatorClient.deregisterJob(jobId);
        } catch (ScheduleNotFound e) {
            log.info("Job does not exist. No deregister needed, jobId={}", jobId, e);
        } catch (TException e) {
            log.error("Exception when trying to deregister job, jobId={}", jobId, e);
        }
    }
}
