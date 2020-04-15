package com.rbkmoney.threeds.server.handle.constraint.pprq.threedsservertransid;

import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.pprq.PPrqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PPrqThreeDSServerTransIDContentConstraintValidationHandlerImpl implements PPrqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PPrq o) {
        return stringValidator.isNotNull(o.getThreeDSServerTransID());
    }

    @Override
    public ConstraintValidationResult handle(PPrq o) {
        return stringValidator.validateUUID("threeDSServerTransID", o.getThreeDSServerTransID());
    }
}
