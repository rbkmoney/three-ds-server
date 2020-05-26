package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChallengeFlowTransactionInfoConverter {

    public ChallengeFlowTransactionInfo toDomain(
            com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo thriftTransactionInfo) {
        return ChallengeFlowTransactionInfo.builder()
                .deviceChannel(DeviceChannel.of(thriftTransactionInfo.getDeviceChannel()))
                .decoupledAuthMaxTime(LocalDateTime.parse(thriftTransactionInfo.getDecoupledAuthMaxTime()))
                .acsDecConInd(AcsDecConInd.of(thriftTransactionInfo.getAcsDecConInd()))
                .build();
    }

    public com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo toThrift(
            String threeDSServerTransID,
            ChallengeFlowTransactionInfo domainTransactionInfo) {
        return new com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo()
                .setTransactionId(threeDSServerTransID)
                .setDeviceChannel(domainTransactionInfo.getDeviceChannel().getValue())
                .setDecoupledAuthMaxTime(domainTransactionInfo.getDecoupledAuthMaxTime().toString())
                .setAcsDecConInd(domainTransactionInfo.getAcsDecConInd().getValue());
    }
}
