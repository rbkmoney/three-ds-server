package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.acctid;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcctIdContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getAcctID());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        return stringValidator.validateStringWithMaxLength("acctID", 64, o.getAcctID());
    }
}
