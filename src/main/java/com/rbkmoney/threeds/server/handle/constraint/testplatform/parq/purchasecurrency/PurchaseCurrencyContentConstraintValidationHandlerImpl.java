package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.purchasecurrency;

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
public class PurchaseCurrencyContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getPurchaseCurrency());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String purchaseCurrency = o.getPurchaseCurrency();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithConstLength("purchaseCurrency", 3, purchaseCurrency);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(purchaseCurrency)) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseCurrency");
        }

        return ConstraintValidationResult.success();
    }
}
