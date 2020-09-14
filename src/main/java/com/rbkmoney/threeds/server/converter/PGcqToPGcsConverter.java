package com.rbkmoney.threeds.server.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.CacheService;
import com.rbkmoney.threeds.server.utils.CReqEncoder;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

@Component
@RequiredArgsConstructor
public class PGcqToPGcsConverter implements Converter<ValidationResult, Message> {

    private static final String TITLE = "cReqData";
    private static final String TEMPLATE_PATH = "vm/ChallengeForm.vm";

    private final CacheService cacheService;

    private final VelocityEngine templateEngine;

    private final CReqEncoder cReqEncoder;

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
            return pGcs;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Parse error", ex);
        }
    }

    private CReqData createCReqData(PGcq pGcq) throws JsonProcessingException {
        String encodeCReq = cReqEncoder.createAndEncodeCReq(pGcq);

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

    @Data
    @Builder
    public static class CReqData {

        private String acsURL;
        private String encodeCreq;
        private String threeDSSessionData;

    }
}
