package com.rbkmoney.threeds.server.handle.constraint.testplatform.pprq;

import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;

@Component
@RequiredArgsConstructor
public class PPrqRequiredContentConstraintValidationHandlerImpl implements PPrqConstraintValidationHandler {

    @Override
    public boolean canHandle(PPrq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PPrq o) {
        if (o.getThreeDSServerTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSServerTransID");
        }

        return ConstraintValidationResult.success();
    }
}
