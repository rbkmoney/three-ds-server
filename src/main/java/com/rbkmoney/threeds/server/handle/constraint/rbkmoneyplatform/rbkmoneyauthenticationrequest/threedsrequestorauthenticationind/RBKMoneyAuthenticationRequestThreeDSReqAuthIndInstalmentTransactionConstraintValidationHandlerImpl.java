package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.threedsrequestorauthenticationind;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.INSTALMENT_TRANSACTION;
import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestThreeDSReqAuthIndInstalmentTransactionConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return getValue(o.getThreeDSRequestorAuthenticationInd()) == INSTALMENT_TRANSACTION;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        MessageCategory messageCategory = getValue(o.getMessageCategory());

        if (o.getPurchaseInstalData() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseInstalData");
        }

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
            ConstraintValidationResult validationResult = validateRequiredConditionField(o.getPurchaseDate(), "purchaseDate");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        ConstraintValidationResult validationResult = validateRequiredConditionField(o.getRecurringExpiry(), "recurringExpiry");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (o.getRecurringFrequency() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "recurringFrequency");
        }

        return ConstraintValidationResult.success();
    }
}
