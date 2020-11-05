package com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.sdktransid;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.RReqConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RReqSdkTransIDContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RReq o) {
        return stringValidator.isNotNull(o.getSdkTransID());
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        return stringValidator.validateUUID("sdkTransID", o.getSdkTransID());
    }
}
