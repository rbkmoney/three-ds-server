package com.rbkmoney.threeds.server.handle.constraint.commonplatform.erro.acstransid;

import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.erro.ErroWrapperConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErroAcsTransIDContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ErroWrapper o) {
        return stringValidator.isNotNull(o.getAcsTransID());
    }

    @Override
    public ConstraintValidationResult handle(ErroWrapper o) {
        return stringValidator.validateUUID("acsTransID", o.getAcsTransID());
    }
}
