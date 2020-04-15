package com.rbkmoney.threeds.server.handle.constraint.ares.acssignedcontent;

import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcsSignedContentContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ARes o) {
        return stringValidator.isNotNull(o.getAcsSignedContent());
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        return stringValidator.validateStringOnlyBlank("acsSignedContent", o.getAcsSignedContent());
    }
}
