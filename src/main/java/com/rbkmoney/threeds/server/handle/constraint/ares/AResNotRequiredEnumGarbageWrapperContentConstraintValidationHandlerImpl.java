package com.rbkmoney.threeds.server.handle.constraint.ares;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.*;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class AResNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        MessageCategory messageCategory = ((AReq) o.getRequestMessage()).getMessageCategory();

        if (messageCategory != MessageCategory.PAYMENT_AUTH
                && getGarbageValue(o.getTransStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        TransactionStatus transactionStatus = getValue(o.getTransStatus());

        if (!isRequiredCondition(messageCategory, transactionStatus)
                && getGarbageValue(o.getTransStatusReason()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transStatusReason");
        }

        if (!(transactionStatus == TransactionStatus.CHALLENGE_REQUIRED
                || transactionStatus == TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED_AUTH)) {
            if (getGarbageValue(o.getAcsChallengeMandated()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "acsChallengeMandated");
            }

            if (getGarbageValue(o.getAuthenticationType()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "authenticationType");
            }
        }

        if (transactionStatus != TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED_AUTH
                && getGarbageValue(o.getAcsDecConInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acsDecConInd");
        }

        if (getValue(o.getWhiteListStatusSource()) == null && getGarbageValue(o.getWhiteListStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        if (getValue(o.getWhiteListStatus()) == null && getGarbageValue(o.getWhiteListStatusSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatusSource");
        }

        return ConstraintValidationResult.success();
    }

    private boolean isRequiredCondition(MessageCategory messageCategory, TransactionStatus transactionStatus) {
        return (transactionStatus == AUTHENTICATION_REJECTED || transactionStatus == TECHNICAL_PROBLEM || transactionStatus == NOT_AUTHENTICATED_DENIED)
                && messageCategory == MessageCategory.PAYMENT_AUTH;
    }
}
