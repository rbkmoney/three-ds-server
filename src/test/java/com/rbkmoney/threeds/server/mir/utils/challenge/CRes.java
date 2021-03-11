package com.rbkmoney.threeds.server.mir.utils.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@SuppressWarnings({"checkstyle:abbreviationaswordinname"})
public class CRes {

    private String acsTransID;
    private String acsUiType;
    private String acsHTML;
    private String acsCounterAtoS;
    private String challengeCompletionInd;
    private String messageType;
    private String messageVersion;
    private String sdkTransID;
    private String threeDSServerTransID;
    private String transStatus;

}
