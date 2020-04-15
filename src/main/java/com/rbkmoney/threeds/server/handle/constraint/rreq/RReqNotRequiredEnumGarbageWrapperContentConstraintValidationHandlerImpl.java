package com.rbkmoney.threeds.server.handle.constraint.rreq;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.*;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperGarbageValue;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;

@Component
@RequiredArgsConstructor
public class RReqNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        TransactionStatus transactionStatus = getEnumWrapperValue(o.getTransStatus());
        MessageCategory messageCategory = getEnumWrapperValue(o.getMessageCategory());

        if (messageCategory != MessageCategory.PAYMENT_AUTH
                && getEnumWrapperGarbageValue(o.getTransStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        if (!isRequiredCondition(transactionStatus, messageCategory)
                && getEnumWrapperGarbageValue(o.getTransStatusReason()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transStatusReason");
        }

        if (getEnumWrapperGarbageValue(o.getAuthenticationMethod()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "authenticationMethod");
        }

        if (!(transactionStatus == AUTHENTICATION_VERIFICATION_SUCCESSFUL
                || transactionStatus == NOT_AUTHENTICATED_DENIED)
                && getEnumWrapperGarbageValue(o.getAuthenticationType()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "authenticationType");
        }

        if (getEnumWrapperValue(o.getTransStatusReason()) != TransactionStatusReason.TRANSACTION_TIMED_OUT_AT_ACS
                && getEnumWrapperGarbageValue(o.getChallengeCancel()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "challengeCancel");
        }

        if (getEnumWrapperValue(o.getWhiteListStatusSource()) == null && getEnumWrapperGarbageValue(o.getWhiteListStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        if (getEnumWrapperValue(o.getWhiteListStatus()) == null && getEnumWrapperGarbageValue(o.getWhiteListStatusSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatusSource");
        }

        return ConstraintValidationResult.success();
    }

    private boolean isRequiredCondition(TransactionStatus transactionStatus, MessageCategory messageCategory) {
        return (transactionStatus == AUTHENTICATION_REJECTED || transactionStatus == TECHNICAL_PROBLEM || transactionStatus == NOT_AUTHENTICATED_DENIED)
                && messageCategory == MessageCategory.PAYMENT_AUTH;
    }
}
