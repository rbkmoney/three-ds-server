package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.purchaseamount;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class PurchaseAmountContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getPurchaseAmount());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String purchaseAmount = o.getPurchaseAmount();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("purchaseAmount", 48, purchaseAmount);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(purchaseAmount)) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseAmount");
        }

        return ConstraintValidationResult.success();
    }
}
