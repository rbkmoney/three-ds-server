package com.rbkmoney.threeds.server.handle.constraint.testplatform.pgcq.acstransid;

import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.pgcq.PGcqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGcqAcsTransIDContentConstraintValidationHandlerImpl implements PGcqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PGcq o) {
        return stringValidator.isNotNull(o.getAcsTransID());
    }

    @Override
    public ConstraintValidationResult handle(PGcq o) {
        return stringValidator.validateUUID("acsTransID", o.getAcsTransID());
    }
}
