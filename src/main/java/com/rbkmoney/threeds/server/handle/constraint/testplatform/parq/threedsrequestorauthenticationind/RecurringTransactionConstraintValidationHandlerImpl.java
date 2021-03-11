package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.threedsrequestorauthenticationind;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.RECURRING_TRANSACTION;
import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RecurringTransactionConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return getValue(o.getThreeDSRequestorAuthenticationInd()) == RECURRING_TRANSACTION;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        MessageCategory messageCategory = getValue(o.getMessageCategory());

        if (messageCategory == MessageCategory.NON_PAYMENT_AUTH
                && o.getPurchaseAmount() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseAmount");
        }

        if (messageCategory == MessageCategory.NON_PAYMENT_AUTH
                && o.getPurchaseCurrency() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseCurrency");
        }

        if (messageCategory == MessageCategory.NON_PAYMENT_AUTH
                && o.getPurchaseExponent() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseExponent");
        }

        if (messageCategory == MessageCategory.NON_PAYMENT_AUTH) {
            ConstraintValidationResult validationResult =
                    validateRequiredConditionField(o.getPurchaseDate(), "purchaseDate");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        ConstraintValidationResult validationResult =
                validateRequiredConditionField(o.getRecurringExpiry(), "recurringExpiry");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (o.getRecurringFrequency() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "recurringFrequency");
        }

        return ConstraintValidationResult.success();
    }
}
