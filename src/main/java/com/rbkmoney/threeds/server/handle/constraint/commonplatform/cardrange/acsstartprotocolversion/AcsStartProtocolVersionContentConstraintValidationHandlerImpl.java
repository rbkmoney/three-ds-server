package com.rbkmoney.threeds.server.handle.constraint.commonplatform.cardrange.acsstartprotocolversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class AcsStartProtocolVersionContentConstraintValidationHandlerImpl
        implements CardRangeConstraintValidationHandler {

    private final StringValidator stringValidator;
    private final EnvironmentMessageProperties messageProperties;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getAcsStartProtocolVersion());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator
                .validateStringWithMinAndMaxLength("acsStartProtocolVersion", 8, 5, o.getAcsStartProtocolVersion());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!messageProperties.getValidMessageVersions().contains(o.getAcsStartProtocolVersion())) {
            return ConstraintValidationResult.failure(PATTERN, "acsStartProtocolVersion");
        }

        return ConstraintValidationResult.success();
    }
}
