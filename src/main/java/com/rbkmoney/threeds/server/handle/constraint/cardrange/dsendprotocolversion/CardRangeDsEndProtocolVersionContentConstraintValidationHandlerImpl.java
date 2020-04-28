package com.rbkmoney.threeds.server.handle.constraint.cardrange.dsendprotocolversion;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class CardRangeDsEndProtocolVersionContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    private final StringValidator stringValidator;
    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public boolean canHandle(CardRange o) {
        return stringValidator.isNotNull(o.getDsEndProtocolVersion());
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("dsEndProtocolVersion", 8, 5, o.getDsEndProtocolVersion());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!providerHolder.getEnvironmentProperties().getValidMessageVersions().contains(o.getDsEndProtocolVersion())) {
            return ConstraintValidationResult.failure(PATTERN, "dsEndProtocolVersion");
        }

        return ConstraintValidationResult.success();
    }
}
