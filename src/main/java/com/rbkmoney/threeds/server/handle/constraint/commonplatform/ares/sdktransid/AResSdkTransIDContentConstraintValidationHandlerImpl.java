package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.sdktransid;

import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.ID_NOT_RECOGNISED;

@Component
@RequiredArgsConstructor
public class AResSdkTransIDContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ARes o) {
        return stringValidator.isNotNull(o.getSdkTransID());
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        ConstraintValidationResult validationResult = stringValidator.validateUUID("sdkTransID", o.getSdkTransID());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!o.getSdkTransID().equals(o.getRequestMessage().getSdkTransID())) {
            return ConstraintValidationResult.failure(ID_NOT_RECOGNISED, "sdkTransID");
        }

        return ConstraintValidationResult.success();
    }
}
