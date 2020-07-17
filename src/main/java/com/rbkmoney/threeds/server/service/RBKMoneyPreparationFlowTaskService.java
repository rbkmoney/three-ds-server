package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class RBKMoneyPreparationFlowTaskService {

    private final CardRangesStorageSrv.Iface cardRangesStorage;

    @Value("${preparation-flow.on-startup.enabled}")
    private boolean isEnabledOnStartup;

    @Value("${preparation-flow.on-schedule.enabled}")
    private boolean isEnabledOnSchedule;

    @PostConstruct
    public void onStartup() {
        if (isEnabledOnStartup) {
            initRBKMoneyPreparationFlow();
        }
    }

    @Scheduled(cron = "${preparation-flow.on-schedule.cron}")
    public void onSchedule() {
        if (isEnabledOnSchedule) {
            initRBKMoneyPreparationFlow();
        }
    }

    private void initRBKMoneyPreparationFlow() {
        for (DsProvider dsProvider : DsProvider.values()) {
            try {
                InitRBKMoneyPreparationFlowRequest request = new InitRBKMoneyPreparationFlowRequest()
                        .setProviderId(dsProvider.getId());
                cardRangesStorage.initRBKMoneyPreparationFlow(request);
            } catch (TException e) {
                throw new ThreeDsServerStorageException(e);
            }
        }
    }
}
