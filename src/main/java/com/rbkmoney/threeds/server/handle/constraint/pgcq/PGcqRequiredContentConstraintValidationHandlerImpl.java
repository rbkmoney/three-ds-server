package com.rbkmoney.threeds.server.handle.constraint.pgcq;

import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;

@Component
@RequiredArgsConstructor
public class PGcqRequiredContentConstraintValidationHandlerImpl implements PGcqConstraintValidationHandler {

    @Override
    public boolean canHandle(PGcq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PGcq o) {
        if (o.getThreeDSServerTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSServerTransID");
        }

        if (o.getAcsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsTransID");
        }

        return ConstraintValidationResult.success();
    }
}
