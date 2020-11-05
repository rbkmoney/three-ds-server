package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.acstransid;

import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AResAcsTransIDContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ARes o) {
        return stringValidator.isNotNull(o.getAcsTransID());
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        return stringValidator.validateUUID("acsTransID", o.getAcsTransID());
    }
}
