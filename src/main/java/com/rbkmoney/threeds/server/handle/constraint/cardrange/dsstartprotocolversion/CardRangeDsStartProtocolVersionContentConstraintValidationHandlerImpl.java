package com.rbkmoney.threeds.server.handle.constraint.cardrange.dsstartprotocolversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class CardRangeDsStartProtocolVersionContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    private final StringValidator stringValidator;
    private final EnvironmentMessageProperties messageProperties;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getDsStartProtocolVersion());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("dsStartProtocolVersion", 8, 5, o.getDsStartProtocolVersion());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!messageProperties.getValidMessageVersions().contains(o.getDsStartProtocolVersion())) {
            return ConstraintValidationResult.failure(PATTERN, "dsStartProtocolVersion");
        }

        return ConstraintValidationResult.success();
    }
}
