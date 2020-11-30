package com.rbkmoney.threeds.server.converter.commonplatform;

import com.rbkmoney.threeds.server.domain.challenge.ChallengeCancel;
import com.rbkmoney.threeds.server.domain.result.ResultsStatus;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.domain.root.emvco.RRes;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@RequiredArgsConstructor
public class RReqToRResConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult validationResult) {
        RReq rReq = (RReq) validationResult.getMessage();

        RRes rRes = RRes.builder()
                .threeDSServerTransID(rReq.getThreeDSServerTransID())
                .acsTransID(rReq.getAcsTransID())
                .dsTransID(rReq.getDsTransID())
                .messageExtension(getValue(rReq.getMessageExtension()))
                .resultsStatus(analyzeRReqMessageStatus(rReq))
                .sdkTransID(rReq.getSdkTransID())
                .build();
        rRes.setMessageVersion(rReq.getMessageVersion());
        return rRes;
    }

    private ResultsStatus analyzeRReqMessageStatus(RReq message) {
        ChallengeCancel challengeCancel = getValue(message.getChallengeCancel());
        if (challengeCancel == null) {
            return ResultsStatus.RREQ_RECEIVED_FOR_FURTHER_PROCESSING;
        }

        switch (challengeCancel) {
            case CARDHOLDER_SELECTED_CANCEL:
            case TRANSACTION_TIMED_OUT_OTHER_TIMEOUTS:
            case TRANSACTION_ERROR:
            case UNKNOWN:
            case TRANSACTION_TIMED_OUT_AT_SDK:
            case TRANSACTION_TIMED_OUT_DECOUPLED_AUTH:
                return ResultsStatus.RREQ_RECEIVED_FOR_FURTHER_PROCESSING;
            case TRANSACTION_TIMED_OUT_FIRST_CREQ_NOT_RECEIVED:
                return ResultsStatus.CREQ_NOT_SENT_TO_ACS;
            default:
                throw new IllegalArgumentException(String.format("ChallengeCancel is reserved value, unsupported, challengeCancel='%s'", message.getChallengeCancel()));
        }
    }
}
