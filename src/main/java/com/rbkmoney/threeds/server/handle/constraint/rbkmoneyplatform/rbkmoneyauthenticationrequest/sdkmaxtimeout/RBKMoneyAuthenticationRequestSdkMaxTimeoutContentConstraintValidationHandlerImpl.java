package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.sdkmaxtimeout;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestSdkMaxTimeoutContentConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return stringValidator.isNotNull(o.getSdkMaxTimeout());
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        String sdkMaxTimeout = o.getSdkMaxTimeout();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithConstLength("sdkMaxTimeout", 2, sdkMaxTimeout);
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
