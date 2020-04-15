package com.rbkmoney.threeds.server.handle.constraint.pres;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;

@Component
@RequiredArgsConstructor
public class PResRequiredContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    @Override
    public boolean canHandle(PRes o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        if (o.getThreeDSServerTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSServerTransID");
        }

        if (o.getDsEndProtocolVersion() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "dsEndProtocolVersion");
        }

        if (o.getDsStartProtocolVersion() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "dsStartProtocolVersion");
        }

        if (o.getDsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "dsTransID");
        }

        return ConstraintValidationResult.success();
    }
}
