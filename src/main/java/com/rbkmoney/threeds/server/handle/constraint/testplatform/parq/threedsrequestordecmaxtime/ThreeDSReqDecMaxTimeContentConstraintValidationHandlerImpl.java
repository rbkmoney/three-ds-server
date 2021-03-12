package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.threedsrequestordecmaxtime;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static java.lang.Integer.valueOf;

@Component
@RequiredArgsConstructor
public class ThreeDSReqDecMaxTimeContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getThreeDSRequestorDecMaxTime());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String threeDSRequestorDecMaxTime = o.getThreeDSRequestorDecMaxTime();

        ConstraintValidationResult validationResult = stringValidator
                .validateStringWithConstLength("threeDSRequestorDecMaxTime", 5, threeDSRequestorDecMaxTime);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!NumberUtils.isCreatable(threeDSRequestorDecMaxTime)) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorDecMaxTime");
        }

        Integer value = valueOf(threeDSRequestorDecMaxTime);
        if (value < 1 || value > 10080) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorDecMaxTime");
        }

        return ConstraintValidationResult.success();
    }
}
