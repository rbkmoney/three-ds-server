package com.rbkmoney.threeds.server.handle.constraint.cardrange;

import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperGarbageValue;

@Component
@RequiredArgsConstructor
public class CardRangeNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    @Override
    public boolean canHandle(CardRange o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        if (getEnumWrapperGarbageValue(o.getActionInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "actionInd");
        }

        return ConstraintValidationResult.success();
    }
}
