package com.rbkmoney.threeds.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.challenge.ChallengeWindowSize;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class CReq {

    private String acsTransID;
    private ChallengeWindowSize challengeWindowSize;
    private final String messageType = "CReq";
    private String messageVersion;
    private String threeDSServerTransID;

}
