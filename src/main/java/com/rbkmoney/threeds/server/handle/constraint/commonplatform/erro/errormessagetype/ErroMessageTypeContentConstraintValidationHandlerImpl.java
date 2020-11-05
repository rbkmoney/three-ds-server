package com.rbkmoney.threeds.server.handle.constraint.commonplatform.erro.errormessagetype;

import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.erro.ErroWrapperConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class ErroMessageTypeContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    @Override
    public boolean canHandle(ErroWrapper o) {
        return getValue(o.getErrorMessageType()) != null;
    }

    @Override
    public ConstraintValidationResult handle(ErroWrapper o) {
        return validateRequiredConditionField(o.getErrorMessageType(), "errorMessageType");
    }
}
