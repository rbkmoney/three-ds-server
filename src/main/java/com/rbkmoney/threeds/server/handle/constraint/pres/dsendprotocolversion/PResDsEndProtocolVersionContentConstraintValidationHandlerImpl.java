package com.rbkmoney.threeds.server.handle.constraint.pres.dsendprotocolversion;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.pres.PResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PResDsEndProtocolVersionContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PRes o) {
        return stringValidator.isNotNull(o.getDsEndProtocolVersion());
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        return stringValidator.validateStringWithMinAndMaxLength("dsEndProtocolVersion", 8, 5, o.getDsEndProtocolVersion());
    }
}
