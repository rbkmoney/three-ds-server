package com.rbkmoney.threeds.server.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThreeDsMethodFormData {

    private String threeDsMethodUrl;
    private String encodeThreeDsMethodData;

}
