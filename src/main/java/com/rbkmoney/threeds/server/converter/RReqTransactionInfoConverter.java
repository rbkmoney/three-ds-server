package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RReqTransactionInfoConverter {

    public RReqTransactionInfo toDomain(
            com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfo thriftTransactionInfo) {
        return RReqTransactionInfo.builder()
                .deviceChannel(DeviceChannel.of(thriftTransactionInfo.getDeviceChannel()))
                .decoupledAuthMaxTime(LocalDateTime.parse(thriftTransactionInfo.getDecoupledAuthMaxTime()))
                .acsDecConInd(AcsDecConInd.of(thriftTransactionInfo.getAcsDecConInd()))
                .build();
    }

    public com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfo toThrift(
            String threeDSServerTransID,
            RReqTransactionInfo domainTransactionInfo) {
        return new com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfo()
                .setTransactionId(threeDSServerTransID)
                .setDeviceChannel(domainTransactionInfo.getDeviceChannel().getValue())
                .setDecoupledAuthMaxTime(domainTransactionInfo.getDecoupledAuthMaxTime().toString())
                .setAcsDecConInd(domainTransactionInfo.getAcsDecConInd().getValue());
    }
}
