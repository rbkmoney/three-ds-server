package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class RBKMoneyPreparationFlowTaskService {

    @Value("${preparation-flow.on-startup.enabled}")
    private boolean isEnabledOnStartup;

    @Value("${preparation-flow.on-schedule.enabled}")
    private boolean isEnabledOnSchedule;

    @Value("${preparation-flow.ds-provider-enabled.mastercard}")
    private boolean isMastercardEnabled;

    @Value("${preparation-flow.ds-provider-enabled.visa}")
    private boolean isVisaEnabled;

    @Value("${preparation-flow.ds-provider-enabled.mir}")
    private boolean isMirEnabled;

    private final CardRangesStorageSrv.Iface cardRangesStorage;

    @EventListener(value = ApplicationReadyEvent.class)
    public void onStartup() {
        if (isEnabledOnStartup) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(this::initRBKMoneyPreparationFlow);
        }
    }

    @Scheduled(cron = "${preparation-flow.on-schedule.cron}")
    public void onSchedule() {
        if (isEnabledOnSchedule) {
            initRBKMoneyPreparationFlow();
        }
    }

    private void initRBKMoneyPreparationFlow() {
        if (isMastercardEnabled) {
            initRBKMoneyPreparationFlow(DsProvider.MASTERCARD.getId());
        }
        if (isVisaEnabled) {
            initRBKMoneyPreparationFlow(DsProvider.VISA.getId());
        }
        if (isMirEnabled) {
            initRBKMoneyPreparationFlow(DsProvider.MIR.getId());
        }
    }

    private void initRBKMoneyPreparationFlow(String dsProvider) {
        try {
            InitRBKMoneyPreparationFlowRequest request = new InitRBKMoneyPreparationFlowRequest()
                    .setProviderId(dsProvider);
            cardRangesStorage.initRBKMoneyPreparationFlow(request);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }
}
