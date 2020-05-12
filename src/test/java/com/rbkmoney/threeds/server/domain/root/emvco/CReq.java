package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class CReq {

    private String acsTransID;
    private String challengeWindowSize;
    private String challengeDataEntry;
    private String challengeHTMLDataEntry;
    private String challengeCancel;
    private final String messageType = "CReq";
    private String messageVersion;
    private String threeDSServerTransID;
    private String sdkTransID;
    private String sdkCounterStoA;

}
