package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.purchaseexponent;

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
public class PurchaseExponentContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getPurchaseExponent());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String purchaseExponent = o.getPurchaseExponent();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithConstLength("purchaseExponent", 1, purchaseExponent);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(purchaseExponent)) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseExponent");
        }

        return ConstraintValidationResult.success();
    }
}
