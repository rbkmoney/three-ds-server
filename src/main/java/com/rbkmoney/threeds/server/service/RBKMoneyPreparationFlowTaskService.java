package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.InitRBKMoneyPreparationFlowRequest;
import com.rbkmoney.threeds.server.config.properties.PreparationProperties;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class RBKMoneyPreparationFlowTaskService {

    private final boolean isEnabledOnStartup;
    private final boolean isEnabledOnSchedule;
    private final PreparationProperties visaPreparationProperties;
    private final PreparationProperties mastercardPreparationProperties;
    private final PreparationProperties mirPreparationProperties;
    private final CardRangesStorageSrv.Iface cardRangesStorage;

    @EventListener(value = ApplicationReadyEvent.class)
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
        if (visaPreparationProperties.isEnabled()) {
            initRBKMoneyPreparationFlow(DsProvider.VISA.getId(), visaPreparationProperties.getMessageVersion());
        }
        if (mastercardPreparationProperties.isEnabled()) {
            initRBKMoneyPreparationFlow(DsProvider.MASTERCARD.getId(), mastercardPreparationProperties.getMessageVersion());
        }
        if (mirPreparationProperties.isEnabled()) {
            initRBKMoneyPreparationFlow(DsProvider.MIR.getId(), mirPreparationProperties.getMessageVersion());
        }
    }

    private void initRBKMoneyPreparationFlow(String dsProviderId, String messageVersion) {
        try {
            InitRBKMoneyPreparationFlowRequest request = new InitRBKMoneyPreparationFlowRequest()
                    .setProviderId(dsProviderId)
                    .setMessageVersion(messageVersion);
            cardRangesStorage.initRBKMoneyPreparationFlow(request);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }
}
