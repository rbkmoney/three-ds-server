package com.rbkmoney.threeds.server.handle.constraint.rreq.transactionstatusreason;

import com.rbkmoney.threeds.server.domain.ChallengeCancel;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RReqTimeoutAtAcsConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return getEnumWrapperValue(o.getTransStatusReason()) == TransactionStatusReason.TRANSACTION_TIMED_OUT_AT_ACS;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        ConstraintValidationResult validationResult = validateRequiredConditionField(o.getChallengeCancel(), "challengeCancel");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!(getEnumWrapperValue(o.getChallengeCancel()) == ChallengeCancel.TRANSACTION_TIMED_OUT_OTHER_TIMEOUTS ||
                getEnumWrapperValue(o.getChallengeCancel()) == ChallengeCancel.TRANSACTION_TIMED_OUT_FIRST_CREQ_NOT_RECEIVED)) {
            return ConstraintValidationResult.failure(PATTERN, "challengeCancel");
        }

        return ConstraintValidationResult.success();
    }
}
