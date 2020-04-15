package com.rbkmoney.threeds.server.handle.constraint.parq.browserscreenwidth;

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
public class BrowserScreenWidthContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getBrowserScreenWidth());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String browserScreenWidth = o.getBrowserScreenWidth();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("browserScreenWidth", 6, 1, browserScreenWidth);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(browserScreenWidth)) {
            return ConstraintValidationResult.failure(PATTERN, "browserScreenWidth");
        }

        return ConstraintValidationResult.success();
    }
}
