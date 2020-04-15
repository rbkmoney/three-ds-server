package com.rbkmoney.threeds.server.handle.constraint.rreq.interactioncounter;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractionCounterContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RReq o) {
        return stringValidator.isNotNull(o.getInteractionCounter());
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        return stringValidator.validateStringWithConstLength("interactionCounter", 2, o.getInteractionCounter());
    }
}
