package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.acsurl;

import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class AcsURLContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final UrlValidator urlValidator = new UrlValidator();
    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ARes o) {
        return stringValidator.isNotNull(o.getAcsURL());
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("acsURL", 2048, o.getAcsURL());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!urlValidator.isValid(o.getAcsURL())) {
            return ConstraintValidationResult.failure(PATTERN, "acsURL");
        }

        return ConstraintValidationResult.success();
    }
}
