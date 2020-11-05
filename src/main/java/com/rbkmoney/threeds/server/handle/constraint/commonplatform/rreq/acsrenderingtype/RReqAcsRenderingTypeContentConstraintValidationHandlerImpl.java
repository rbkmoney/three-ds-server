package com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.acsrenderingtype;

import com.rbkmoney.threeds.server.domain.acs.AcsRenderingTypeWrapper;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RReqAcsRenderingTypeContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return o.getAcsRenderingType() != null;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        AcsRenderingTypeWrapper acsRenderingType = o.getAcsRenderingType();

        ConstraintValidationResult validationResult = validateRequiredConditionField(acsRenderingType.getAcsInterface(), "acsRenderingType.acsInterface");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        return validateRequiredConditionField(acsRenderingType.getAcsUiTemplate(), "acsRenderingType.acsUiTemplate");
    }
}
