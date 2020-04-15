package com.rbkmoney.threeds.server.handle.constraint;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.Handler;

public interface ConstraintValidationHandler<T> extends Handler<T, ConstraintValidationResult> {

    default boolean isValidMessageVersion(T message) {
        if (message instanceof Message) {
            String messageVersion = ((Message) message).getMessageVersion();
            if (messageVersion != null) {
                return messageVersion.equals("2.2.0") || messageVersion.equals("2.1.0");
            }

            return false;
        }

        return true;
    }
}
