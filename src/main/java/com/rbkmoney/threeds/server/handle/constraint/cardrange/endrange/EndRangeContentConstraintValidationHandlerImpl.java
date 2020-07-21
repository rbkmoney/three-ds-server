package com.rbkmoney.threeds.server.handle.constraint.cardrange.endrange;

import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class EndRangeContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getEndRange());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("endRange", 19, 13, o.getEndRange());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(o.getEndRange())) {
            return ConstraintValidationResult.failure(PATTERN, "endRange");
        }

        return ConstraintValidationResult.success();
    }
}
