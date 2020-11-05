package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.mcc;

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
public class MccContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getMcc());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String mcc = o.getMcc();
        ConstraintValidationResult validationResult = stringValidator.validateStringWithConstLength("mcc", 4, mcc);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(mcc)) {
            return ConstraintValidationResult.failure(PATTERN, "mcc");
        }

        return ConstraintValidationResult.success();
    }
}
