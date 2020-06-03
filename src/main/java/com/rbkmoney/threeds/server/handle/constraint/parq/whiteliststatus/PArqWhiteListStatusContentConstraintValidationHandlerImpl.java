package com.rbkmoney.threeds.server.handle.constraint.parq.whiteliststatus;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class PArqWhiteListStatusContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return getValue(o.getWhiteListStatus()) != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        ConstraintValidationResult validationResult = validateRequiredConditionField(o.getWhiteListStatus(), "whiteListStatus");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (getValue(o.getWhiteListStatus()) != WhiteListStatus.WHITELISTED &&
                getValue(o.getWhiteListStatus()) != WhiteListStatus.NOT_WHITELISTED) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        return ConstraintValidationResult.success();
    }
}
