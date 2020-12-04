package com.rbkmoney.threeds.server.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeFormData {

    private String acsURL;
    private String encodeCReq;
    private String threeDSSessionData;

}
