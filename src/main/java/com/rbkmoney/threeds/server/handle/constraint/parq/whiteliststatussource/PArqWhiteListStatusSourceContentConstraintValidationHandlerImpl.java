package com.rbkmoney.threeds.server.handle.constraint.parq.whiteliststatussource;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class PArqWhiteListStatusSourceContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return getValue(o.getWhiteListStatusSource()) != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        return validateRequiredConditionField(o.getWhiteListStatus(), "whiteListStatus");
    }
}
