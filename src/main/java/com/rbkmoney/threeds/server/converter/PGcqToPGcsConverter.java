package com.rbkmoney.threeds.server.converter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.ChallengeWindowSize;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PGcqToPGcsConverter implements Converter<ValidationResult, Message> {

    private static final String TITLE = "cReqData";
    private static final String TEMPLATE_PATH = "vm/ChallengeForm.vm";

    private final CacheService cacheService;

    private final VelocityEngine templateEngine;

    private final ObjectMapper objectMapper;

    @Override
    public Message convert(ValidationResult validationResult) {
        try {
            PGcq pGcq = (PGcq) validationResult.getMessage();

            CReqData cReqData = createCReqData(pGcq);

            String htmlCreq = buildTemplate(cReqData);

            PGcs pGcs = PGcs.builder()
                    .p_messageVersion(pGcq.getP_messageVersion())
                    .htmlCreq(htmlCreq)
                    .build();
            pGcs.setMessageVersion(pGcq.getMessageVersion());
            pGcs.setRequestMessage(pGcq);
            return pGcs;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Parse error", ex);
        }
    }

    private CReqData createCReqData(PGcq pGcq) throws JsonProcessingException {
        String encodeCReq = createAndEncodeCReq(pGcq);

        var transactionInfo = cacheService.getChallengeFlowTransactionInfo(pGcq.getThreeDSServerTransID());

        return CReqData.builder()
                .acsURL(transactionInfo.getAcsUrl())
                .encodeCreq(encodeCReq)
                .threeDSSessionData(pGcq.getThreeDSSessionData())
                .build();
    }

    private String buildTemplate(CReqData cReqData) {
        VelocityContext headerContext = new VelocityContext();
        headerContext.put(TITLE, cReqData);

        StringWriter writer = new StringWriter();

        Template template = templateEngine.getTemplate(TEMPLATE_PATH);
        template.merge(headerContext, writer);
        return writer.toString();
    }

    private String createAndEncodeCReq(PGcq pGcq) throws JsonProcessingException {
        CReq cReq = createCReq(pGcq);

        byte[] bytesCReq = objectMapper.writeValueAsBytes(cReq);

        return Base64.getEncoder().encodeToString(bytesCReq);
    }

    private CReq createCReq(PGcq pGcq) {
        return CReq.builder()
                .acsTransID(pGcq.getAcsTransID())
                .challengeWindowSize(pGcq.getChallengeWindowSize().getValue())
                .messageVersion(pGcq.getMessageVersion())
                .threeDSServerTransID(pGcq.getThreeDSServerTransID())
                .build();
    }

    @Data
    @Builder
    public static class CReqData {

        private String acsURL;
        private String encodeCreq;
        private String threeDSSessionData;

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
