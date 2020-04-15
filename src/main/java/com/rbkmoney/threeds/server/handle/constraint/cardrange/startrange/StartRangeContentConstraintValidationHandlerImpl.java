package com.rbkmoney.threeds.server.handle.constraint.cardrange.startrange;

import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class StartRangeContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getStartRange());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("startRange", 19, 13, o.getStartRange());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(o.getStartRange())) {
            return ConstraintValidationResult.failure(PATTERN, "startRange");
        }

        return ConstraintValidationResult.success();
    }
}
