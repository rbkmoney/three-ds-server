package com.rbkmoney.threeds.server.handle.constraint.testplatform.pprq.threedsrequestorid;

import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.pprq.PPrqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PPrqThreeDSReqIdContentConstraintValidationHandlerImpl implements PPrqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PPrq o) {
        return stringValidator.isNotNull(o.getThreeDSRequestorID());
    }

    @Override
    public ConstraintValidationResult handle(PPrq o) {
        return stringValidator.validateStringWithMaxLength("threeDSRequestorID", 35, o.getThreeDSRequestorID());
    }
}
