package com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.*;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class RReqNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        TransactionStatus transactionStatus = getValue(o.getTransStatus());
        MessageCategory messageCategory = getValue(o.getMessageCategory());

        if (messageCategory != MessageCategory.PAYMENT_AUTH
                && getGarbageValue(o.getTransStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        if (!isRequiredCondition(transactionStatus, messageCategory)
                && getGarbageValue(o.getTransStatusReason()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transStatusReason");
        }

        if (getGarbageValue(o.getAuthenticationMethod()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "authenticationMethod");
        }

        if (!(transactionStatus == AUTHENTICATION_VERIFICATION_SUCCESSFUL
                || transactionStatus == NOT_AUTHENTICATED_DENIED)
                && getGarbageValue(o.getAuthenticationType()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "authenticationType");
        }

        if (getValue(o.getTransStatusReason()) != TransactionStatusReason.TRANSACTION_TIMED_OUT_AT_ACS
                && getGarbageValue(o.getChallengeCancel()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "challengeCancel");
        }

        if (getValue(o.getWhiteListStatusSource()) == null && getGarbageValue(o.getWhiteListStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        if (getValue(o.getWhiteListStatus()) == null && getGarbageValue(o.getWhiteListStatusSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatusSource");
        }

        return ConstraintValidationResult.success();
    }

    private boolean isRequiredCondition(TransactionStatus transactionStatus, MessageCategory messageCategory) {
        return (transactionStatus == AUTHENTICATION_REJECTED || transactionStatus == TECHNICAL_PROBLEM || transactionStatus == NOT_AUTHENTICATED_DENIED)
                && messageCategory == MessageCategory.PAYMENT_AUTH;
    }
}
