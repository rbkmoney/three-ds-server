package com.rbkmoney.threeds.server.handle.constraint.ares.transstatus;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.AUTHENTICATION_REJECTED;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class AResAuthRejectedConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return getValue(o.getTransStatus()) == AUTHENTICATION_REJECTED;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        MessageCategory messageCategory = ((AReq) o.getRequestMessage()).getMessageCategory();

        if (messageCategory == MessageCategory.PAYMENT_AUTH) {
            return validateRequiredConditionField(o.getTransStatusReason(), "transStatusReason");
        }

        return ConstraintValidationResult.success();
    }
}
