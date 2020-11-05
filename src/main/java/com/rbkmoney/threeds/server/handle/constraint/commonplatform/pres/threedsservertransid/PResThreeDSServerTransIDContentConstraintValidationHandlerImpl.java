package com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.threedsservertransid;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.PResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.ID_NOT_RECOGNISED;

@Component
@RequiredArgsConstructor
public class PResThreeDSServerTransIDContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PRes o) {
        return stringValidator.isNotNull(o.getThreeDSServerTransID());
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        ConstraintValidationResult validationResult = stringValidator.validateUUID("threeDSServerTransID", o.getThreeDSServerTransID());

        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!o.getThreeDSServerTransID().equals(o.getRequestMessage().getThreeDSServerTransID())) {
            return ConstraintValidationResult.failure(ID_NOT_RECOGNISED, "threeDSServerTransID");
        }

        return ConstraintValidationResult.success();
    }
}
