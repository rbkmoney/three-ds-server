package com.rbkmoney.threeds.server.handle.constraint.pgcq.threedsservertransid;

import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.pgcq.PGcqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGcqThreeDSServerTransIDContentConstraintValidationHandlerImpl implements PGcqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PGcq o) {
        return stringValidator.isNotNull(o.getThreeDSServerTransID());
    }

    @Override
    public ConstraintValidationResult handle(PGcq o) {
        return stringValidator.validateUUID("threeDSServerTransID", o.getThreeDSServerTransID());
    }
}
