package com.rbkmoney.threeds.server.handle.constraint.error.errormessagetype;

import com.rbkmoney.threeds.server.domain.message.MessageType;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.error.ErroWrapperConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class ErrorMessageTypeContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(ErroWrapper o) {
        //todo
        return stringValidator.isNotNull(o.getErrorMessageType().getValue().getValue());
    }

    @Override
    public ConstraintValidationResult handle(ErroWrapper o) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getValue().equals(o.getErrorMessageType())) {
                return ConstraintValidationResult.success();
            }
        }

        return ConstraintValidationResult.failure(PATTERN, "errorMessageType");
    }
}
