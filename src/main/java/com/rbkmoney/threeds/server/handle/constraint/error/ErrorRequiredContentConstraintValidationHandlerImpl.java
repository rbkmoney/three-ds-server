package com.rbkmoney.threeds.server.handle.constraint.error;

import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class ErrorRequiredContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    @Override
    public boolean canHandle(ErroWrapper o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(ErroWrapper o) {
        ConstraintValidationResult validationResult = validateRequiredConditionField(o.getErrorCode(), "errorCode");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        validationResult = validateRequiredConditionField(o.getErrorComponent(), "errorComponent");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (o.getErrorDescription() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "errorDescription");
        }

        if (o.getErrorDetail() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "errorDetail");
        }

        return ConstraintValidationResult.success();
    }
}
