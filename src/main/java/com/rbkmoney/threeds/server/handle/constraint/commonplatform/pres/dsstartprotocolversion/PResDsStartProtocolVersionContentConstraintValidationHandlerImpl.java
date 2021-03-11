package com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.dsstartprotocolversion;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.PResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PResDsStartProtocolVersionContentConstraintValidationHandlerImpl
        implements PResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PRes o) {
        return stringValidator.isNotNull(o.getDsStartProtocolVersion());
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        return stringValidator
                .validateStringWithMinAndMaxLength("dsStartProtocolVersion", 8, 5, o.getDsStartProtocolVersion());
    }
}
