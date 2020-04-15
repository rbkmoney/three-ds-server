package com.rbkmoney.threeds.server.handle.constraint.pres.dstransid;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.pres.PResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PResDsTransIDContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PRes o) {
        return stringValidator.isNotNull(o.getDsTransID());
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        return stringValidator.validateUUID("dsTransID", o.getDsTransID());
    }
}
