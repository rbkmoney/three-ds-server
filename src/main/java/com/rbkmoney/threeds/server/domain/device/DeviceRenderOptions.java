package com.rbkmoney.threeds.server.domain.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.threeds.server.domain.sdk.SdkInterface;
import com.rbkmoney.threeds.server.domain.sdk.SdkUiType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class DeviceRenderOptions {

    @JsonProperty("sdkInterface")
    private SdkInterface sdkInterface;
    @JsonProperty("sdkUiType")
    private List<SdkUiType> sdkUiType;

}
