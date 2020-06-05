package com.rbkmoney.threeds.server.handle.constraint.rreq.transstatus;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.TECHNICAL_PROBLEM;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RReqAuthProblemConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return getValue(o.getTransStatus()) == TECHNICAL_PROBLEM;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        if (getValue(o.getMessageCategory()) == MessageCategory.PAYMENT_AUTH) {
            return validateRequiredConditionField(o.getTransStatusReason(), "transStatusReason");
        }

        return ConstraintValidationResult.success();
    }
}
