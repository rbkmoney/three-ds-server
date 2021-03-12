package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.purchaseinstaldata;

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
public class PurchaseInstalDataContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getPurchaseInstalData());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String purchaseInstalData = o.getPurchaseInstalData();
        ConstraintValidationResult validationResult =
                stringValidator.validateStringWithMaxLength("purchaseInstalData", 3, purchaseInstalData);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(purchaseInstalData)) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseInstalData");
        }

        Integer value = Integer.valueOf(purchaseInstalData);
        if (value <= 1) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseInstalData");
        }

        return ConstraintValidationResult.success();
    }
}
