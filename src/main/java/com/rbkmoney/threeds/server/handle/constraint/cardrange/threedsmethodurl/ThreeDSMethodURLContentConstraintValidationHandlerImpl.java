package com.rbkmoney.threeds.server.handle.constraint.cardrange.threedsmethodurl;

import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class ThreeDSMethodURLContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    private final UrlValidator urlValidator = new UrlValidator();
    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getThreeDSMethodURL());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("threeDSMethodURL", 256, o.getThreeDSMethodURL());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!urlValidator.isValid(o.getThreeDSMethodURL())) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSMethodURL");
        }

        return validationResult;
    }
}
