package com.rbkmoney.threeds.server.handle.constraint.ares.acsoperatorid;

import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcsOperatorIDContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ARes o) {
        return stringValidator.isNotNull(o.getAcsOperatorID());
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        return stringValidator.validateStringWithMaxLength("acsOperatorID", 32, o.getAcsOperatorID());
    }
}
