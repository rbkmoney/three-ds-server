package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.domain.ChallengeCancel;
import com.rbkmoney.threeds.server.domain.ResultsStatus;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.domain.root.emvco.RRes;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getListWrapperValue;

@Component
@RequiredArgsConstructor
public class RReqToRResConverter implements Converter<ValidationResult, Message> {

    private final CacheService cacheService;

    @Override
    public Message convert(ValidationResult validationResult) {
        RReq rReq = (RReq) validationResult.getMessage();

        RRes rRes = RRes.builder()
                .threeDSServerTransID(rReq.getThreeDSServerTransID())
                .acsTransID(rReq.getAcsTransID())
                .dsTransID(rReq.getDsTransID())
                .messageExtension(getListWrapperValue(rReq.getMessageExtension()))
                .resultsStatus(analyzeRReqMessageStatus(rReq))
                .sdkTransID(rReq.getSdkTransID())
                .build();
        rRes.setMessageVersion(rReq.getMessageVersion());

        cacheService.clearRReqTransactionInfo(rReq.getThreeDSServerTransID());

        return rRes;
    }

    private ResultsStatus analyzeRReqMessageStatus(RReq message) {
        ChallengeCancel challengeCancel = getEnumWrapperValue(message.getChallengeCancel());
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
                throw new NotImplementedException(String.format("This is reserved value, challengeCancel='%s'", message.getChallengeCancel()));
        }
    }
}
