package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.cardholderinfo;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class CardholderInfoContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ARes o) {
        return stringValidator.isNotNull(o.getCardholderInfo());
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        AcsDecConInd acsDecConInd = getValue(o.getAcsDecConInd());
        ConstraintValidationResult validationResult =
                stringValidator.validateStringWithMaxLength("cardholderInfo", 128, o.getCardholderInfo());

        if (acsDecConInd == AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED) {
            return validationResult;
        }

        if (!validationResult.isValid()) {
            return ConstraintValidationResult.failure(PATTERN, "cardholderInfo");
        }

        return ConstraintValidationResult.success();
    }
}
