package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.sdkmaxtimeout;

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
public class SdkMaxTimeoutContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getSdkMaxTimeout());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String sdkMaxTimeout = o.getSdkMaxTimeout();

        ConstraintValidationResult validationResult =
                stringValidator.validateStringWithConstLength("sdkMaxTimeout", 2, sdkMaxTimeout);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(sdkMaxTimeout)) {
            return ConstraintValidationResult.failure(PATTERN, "sdkMaxTimeout");
        }

        Integer value = Integer.valueOf(sdkMaxTimeout);
        if (value < 5) {
            return ConstraintValidationResult.failure(PATTERN, "sdkMaxTimeout");
        }

        return ConstraintValidationResult.success();
    }
}
