package com.rbkmoney.threeds.server.converter.testplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.dto.CReq;
import com.rbkmoney.threeds.server.dto.ChallengeFormData;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.utils.Base64Encoder;
import com.rbkmoney.threeds.server.utils.TemplateBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:parametername", "checkstyle:localvariablename"})
public class PGcqToPGcsConverter implements Converter<ValidationResult, Message> {

    private static final String TITLE = "challengeFormData";
    private static final String TEMPLATE_PATH = "vm/ChallengeForm.vm";

    private final TestPlatformChallengeFlowTransactionInfoStorageService
            testPlatformChallengeFlowTransactionInfoStorageService;
    private final TemplateBuilder templateBuilder;
    private final Base64Encoder base64Encoder;

    @Override
    public Message convert(ValidationResult validationResult) {
        PGcq pGcq = (PGcq) validationResult.getMessage();

        ChallengeFormData challengeFormData = createChallengeFormData(pGcq);

        String htmlCreq = templateBuilder.buildTemplate(
                TEMPLATE_PATH,
                velocityContext -> velocityContext.put(TITLE, challengeFormData));

        PGcs pGcs = PGcs.builder()
                .p_messageVersion(pGcq.getP_messageVersion())
                .htmlCreq(htmlCreq)
                .build();
        pGcs.setMessageVersion(pGcq.getMessageVersion());
        pGcq.setUlTestCaseId(pGcq.getUlTestCaseId());
        return pGcs;
    }

    private ChallengeFormData createChallengeFormData(PGcq pGcq) {
        var transactionInfo = testPlatformChallengeFlowTransactionInfoStorageService
                .getChallengeFlowTransactionInfo(pGcq.getThreeDSServerTransID());

        return ChallengeFormData.builder()
                .acsUrl(transactionInfo.getAcsUrl())
                .encodeCreq(base64Encoder.encode(creq(pGcq)))
                .threeDsSessionData(pGcq.getThreeDSSessionData())
                .build();
    }

    private CReq creq(PGcq pGcq) {
        return CReq.builder()
                .acsTransID(pGcq.getAcsTransID())
                .challengeWindowSize(pGcq.getChallengeWindowSize().getValue())
                .messageVersion(pGcq.getMessageVersion())
                .threeDSServerTransID(pGcq.getThreeDSServerTransID())
                .build();
    }
}
