package com.rbkmoney.threeds.server.handle.constraint.rreq.transstatus;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.NOT_AUTHENTICATED_DENIED;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RReqAuthDeniedConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return getValue(o.getTransStatus()) == NOT_AUTHENTICATED_DENIED;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        if (getValue(o.getMessageCategory()) == MessageCategory.PAYMENT_AUTH) {
            ConstraintValidationResult validationResult = validateRequiredConditionField(o.getTransStatusReason(), "transStatusReason");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        return validateRequiredConditionField(o.getAuthenticationType(), "authenticationType");
    }
}
