package com.rbkmoney.threeds.server.handle.constraint.cardrange.acsendprotocolversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class AcsEndProtocolVersionContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    private final StringValidator stringValidator;
    private final EnvironmentProperties environmentProperties;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getAcsEndProtocolVersion());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("acsEndProtocolVersion", 8, 5, o.getAcsEndProtocolVersion());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!environmentProperties.getValidMessageVersions().contains(o.getAcsEndProtocolVersion())) {
            return ConstraintValidationResult.failure(PATTERN, "acsEndProtocolVersion");
        }

        return ConstraintValidationResult.success();
    }
}
