package com.rbkmoney.threeds.server.handle.constraint.pprq.messageextension;

import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.pprq.PPrqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rbkmoney.threeds.server.dto.ConstraintType.CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.MessageExtensions.*;

@Component
@RequiredArgsConstructor
public class PPrqMessageExtensionContentConstraintValidationHandlerImpl implements PPrqConstraintValidationHandler {

    @Override
    public boolean canHandle(PPrq o) {
        return o.getMessageExtension() != null;
    }


    @Override
    public ConstraintValidationResult handle(PPrq o) {
        if (o.getMessageExtension().isGarbage()) {
            return ConstraintValidationResult.failure(PATTERN, "messageExtension");
        }

        List<MessageExtension> messageExtension = o.getMessageExtension().getValue();

        if (messageExtension.isEmpty()) {
            return ConstraintValidationResult.failure(PATTERN, "messageExtension");
        }

        if (isInvalidSize(messageExtension)) {
            return ConstraintValidationResult.failure(PATTERN, "messageExtension");
        }

        // Test Harness Specification: 2.9 Message Extensions
        if (isCriticalMessage(messageExtension)) {
            return ConstraintValidationResult.failure(CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED, getIdCriticalMessage(messageExtension));
        }

        if (isInvalidId(messageExtension)) {
            return ConstraintValidationResult.failure(PATTERN, "messageExtension");
        }

        if (isInvalidName(messageExtension)) {
            return ConstraintValidationResult.failure(PATTERN, "messageExtension");
        }

        if (isInvalidData(messageExtension)) {
            return ConstraintValidationResult.failure(PATTERN, "messageExtension");
        }

        return ConstraintValidationResult.success();
    }
}
