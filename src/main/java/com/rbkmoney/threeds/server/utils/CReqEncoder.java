package com.rbkmoney.threeds.server.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.ChallengeWindowSize;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CReqEncoder {

    private final ObjectMapper objectMapper;

    public String createAndEncodeCReq(RBKMoneyGetChallengeRequest request) throws JsonProcessingException {
        CReq cReq = CReq.builder()
                .acsTransID(request.getAcsTransID())
                .challengeWindowSize(request.getChallengeWindowSize().getValue())
                .messageVersion(request.getMessageVersion())
                .threeDSServerTransID(request.getThreeDSServerTransID())
                .build();

        return encodeCReq(cReq);
    }

    public String createAndEncodeCReq(PGcq pGcq) throws JsonProcessingException {
        CReq cReq = CReq.builder()
                .acsTransID(pGcq.getAcsTransID())
                .challengeWindowSize(pGcq.getChallengeWindowSize().getValue())
                .messageVersion(pGcq.getMessageVersion())
                .threeDSServerTransID(pGcq.getThreeDSServerTransID())
                .build();

        return encodeCReq(cReq);
    }

    private String encodeCReq(CReq cReq) throws JsonProcessingException {
        byte[] bytesCReq = objectMapper.writeValueAsBytes(cReq);

        return Base64.getEncoder().encodeToString(bytesCReq);
    }

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(value = JsonInclude.Include.NON_ABSENT)
    private static class CReq {

        private String acsTransID;
        private ChallengeWindowSize challengeWindowSize;
        private final String messageType = "CReq";
        private String messageVersion;
        private String threeDSServerTransID;

    }
}
