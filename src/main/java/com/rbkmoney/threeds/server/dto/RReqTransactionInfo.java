package com.rbkmoney.threeds.server.dto;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RReqTransactionInfo {

    private DeviceChannel deviceChannel;
    private LocalDateTime decoupledAuthMaxTime;
    private AcsDecConInd acsDecConInd;

}
