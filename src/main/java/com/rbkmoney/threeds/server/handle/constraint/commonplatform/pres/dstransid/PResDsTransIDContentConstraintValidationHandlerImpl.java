package com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.dstransid;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.PResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
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
