package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.browsertz;

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
public class BrowserTZContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getBrowserTZ());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String browserTZ = o.getBrowserTZ();
        ConstraintValidationResult validationResult =
                stringValidator.validateStringWithMinAndMaxLength("browserTZ", 5, 1, browserTZ);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(browserTZ)) {
            return ConstraintValidationResult.failure(PATTERN, "browserTZ");
        }

        return ConstraintValidationResult.success();
    }
}
