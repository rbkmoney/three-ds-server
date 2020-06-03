package com.rbkmoney.threeds.server.handle.constraint.ares.acsrenderingtype;

import com.rbkmoney.threeds.server.domain.acs.AcsRenderingTypeWrapper;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class AResAcsRenderingTypeContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return o.getAcsRenderingType() != null;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        AcsRenderingTypeWrapper acsRenderingType = o.getAcsRenderingType();

        ConstraintValidationResult validationResult = validateRequiredConditionField(acsRenderingType.getAcsInterface(), "acsRenderingType.acsInterface");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        return validateRequiredConditionField(acsRenderingType.getAcsUiTemplate(), "acsRenderingType.acsUiTemplate");
    }
}
