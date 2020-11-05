package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChallengeFlowTransactionInfoConverter {

    public ChallengeFlowTransactionInfo toDomain(
            com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo thriftTransactionInfo) {
        return ChallengeFlowTransactionInfo.builder()
                .threeDSServerTransID(thriftTransactionInfo.getTransactionId())
                .deviceChannel(DeviceChannel.of(thriftTransactionInfo.getDeviceChannel()))
                .decoupledAuthMaxTime(LocalDateTime.parse(thriftTransactionInfo.getDecoupledAuthMaxTime()))
                .acsDecConInd(AcsDecConInd.of(thriftTransactionInfo.getAcsDecConInd()))
                .dsProviderId(thriftTransactionInfo.getProviderId())
                .messageVersion(thriftTransactionInfo.getMessageVersion())
                .acsUrl(thriftTransactionInfo.getAcsUrl())
                .build();
    }

    public com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo toThrift(ChallengeFlowTransactionInfo domainTransactionInfo) {
        return new com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo()
                .setTransactionId(domainTransactionInfo.getThreeDSServerTransID())
                .setDeviceChannel(domainTransactionInfo.getDeviceChannel().getValue())
                .setDecoupledAuthMaxTime(domainTransactionInfo.getDecoupledAuthMaxTime().toString())
                .setAcsDecConInd(domainTransactionInfo.getAcsDecConInd().getValue())
                .setProviderId(domainTransactionInfo.getDsProviderId())
                .setMessageVersion(domainTransactionInfo.getMessageVersion())
                .setAcsUrl(domainTransactionInfo.getAcsUrl());
    }
}
