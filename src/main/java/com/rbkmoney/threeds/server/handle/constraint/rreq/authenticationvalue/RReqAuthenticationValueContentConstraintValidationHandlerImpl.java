package com.rbkmoney.threeds.server.handle.constraint.rreq.authenticationvalue;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RReqAuthenticationValueContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RReq o) {
        return stringValidator.isNotNull(o.getAuthenticationValue());
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        return stringValidator.validateStringWithConstLength("authenticationValue", 28, o.getAuthenticationValue());
    }
}
