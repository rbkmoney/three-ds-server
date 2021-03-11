package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.browserscreenheight;

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
public class BrowserScreenHeightContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getBrowserScreenHeight());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String browserScreenHeight = o.getBrowserScreenHeight();

        ConstraintValidationResult validationResult =
                stringValidator.validateStringWithMinAndMaxLength("browserScreenHeight", 6, 1, browserScreenHeight);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(browserScreenHeight)) {
            return ConstraintValidationResult.failure(PATTERN, "browserScreenHeight");
        }

        return ConstraintValidationResult.success();
    }
}
