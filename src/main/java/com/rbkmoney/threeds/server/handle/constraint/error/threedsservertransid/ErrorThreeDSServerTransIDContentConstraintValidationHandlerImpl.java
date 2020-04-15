package com.rbkmoney.threeds.server.handle.constraint.error.threedsservertransid;

import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.error.ErroWrapperConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorThreeDSServerTransIDContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ErroWrapper o) {
        return stringValidator.isNotNull(o.getThreeDSServerTransID());
    }

    @Override
    public ConstraintValidationResult handle(ErroWrapper o) {
        return stringValidator.validateUUID("threeDSServerTransID", o.getThreeDSServerTransID());
    }
}
