package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
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
