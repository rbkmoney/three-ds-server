package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RReqTransactionInfoConverter {

    public com.rbkmoney.threeds.server.dto.RReqTransactionInfo toDomain(
            com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfo transactionInfo) {
        return com.rbkmoney.threeds.server.dto.RReqTransactionInfo.builder()
                .deviceChannel(DeviceChannel.valueOf(transactionInfo.getDeviceChannel()))
                .decoupledAuthMaxTime(LocalDateTime.parse(transactionInfo.getDecoupledAuthMaxTime()))
                .acsDecConInd(AcsDecConInd.valueOf(transactionInfo.getAcsDecConInd()))
                .build();
    }

    public com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfo toDTO(
            String threeDSServerTransID,
            com.rbkmoney.threeds.server.dto.RReqTransactionInfo transactionInfo) {
        return new com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfo()
                .setTransactionId(threeDSServerTransID)
                .setDeviceChannel(transactionInfo.getDeviceChannel().getValue())
                .setDecoupledAuthMaxTime(transactionInfo.getDecoupledAuthMaxTime().toString())
                .setAcsDecConInd(transactionInfo.getAcsDecConInd().getValue());
    }
}
