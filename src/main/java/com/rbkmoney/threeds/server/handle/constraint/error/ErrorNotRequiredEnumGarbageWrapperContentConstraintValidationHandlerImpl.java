package com.rbkmoney.threeds.server.handle.constraint.error;

import com.rbkmoney.threeds.server.domain.message.MessageType;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class ErrorNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements ErroWrapperConstraintValidationHandler {

    @Override
    public boolean canHandle(ErroWrapper o) {
        return true;
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
