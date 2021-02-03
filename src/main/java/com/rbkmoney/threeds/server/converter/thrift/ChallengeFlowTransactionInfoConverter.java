package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ChallengeFlowTransactionInfoConverter {

    public ChallengeFlowTransactionInfo toDomain(
            com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfo thriftTransactionInfo) {
        return ChallengeFlowTransactionInfo.builder()
                .threeDsServerTransId(thriftTransactionInfo.getTransactionId())
                .deviceChannel(DeviceChannel.of(thriftTransactionInfo.getDeviceChannel()))
                .decoupledAuthMaxTime(LocalDateTime.parse(thriftTransactionInfo.getDecoupledAuthMaxTime()))
                .acsDecConInd(AcsDecConInd.of(thriftTransactionInfo.getAcsDecConInd()))
                .dsProviderId(thriftTransactionInfo.getProviderId())
                .messageVersion(thriftTransactionInfo.getMessageVersion())
                .acsUrl(thriftTransactionInfo.getAcsUrl())
                .build();
    }

    public com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfo toThrift(
            ChallengeFlowTransactionInfo domainTransactionInfo) {
        return new com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfo()
                .setTransactionId(domainTransactionInfo.getThreeDsServerTransId())
                .setDeviceChannel(domainTransactionInfo.getDeviceChannel().getValue())
                .setDecoupledAuthMaxTime(domainTransactionInfo.getDecoupledAuthMaxTime().toString())
                .setAcsDecConInd(Optional.ofNullable(domainTransactionInfo.getAcsDecConInd())
                        .map(AcsDecConInd::getValue)
                        .orElse(null))
                .setProviderId(domainTransactionInfo.getDsProviderId())
                .setMessageVersion(domainTransactionInfo.getMessageVersion())
                .setAcsUrl(domainTransactionInfo.getAcsUrl());
    }
}
