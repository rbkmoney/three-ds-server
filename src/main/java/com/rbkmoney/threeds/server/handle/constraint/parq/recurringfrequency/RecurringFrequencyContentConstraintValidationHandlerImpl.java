package com.rbkmoney.threeds.server.handle.constraint.parq.recurringfrequency;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RecurringFrequencyContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getRecurringFrequency());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String recurringFrequency = o.getRecurringFrequency();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("recurringFrequency", 4, recurringFrequency);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(recurringFrequency)) {
            return ConstraintValidationResult.failure(PATTERN, "recurringFrequency");
        }

        return ConstraintValidationResult.success();
    }
}
