package com.rbkmoney.threeds.server.handle.constraint.error.errormessagetype;

import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.error.ErroWrapperConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class ErrorMessageTypeContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    @Override
    public boolean canHandle(ErroWrapper o) {
        return getValue(o.getErrorMessageType()) != null;
    }

    @Override
    public ConstraintValidationResult handle(ErroWrapper o) {
        return validateRequiredConditionField(o.getErrorMessageType(), "errorMessageType");
    }
}
