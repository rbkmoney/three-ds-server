package com.rbkmoney.threeds.server.handle.constraint.cardrange;

import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;

@Component
@RequiredArgsConstructor
public class CardRangeRequiredContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    @Override
    public boolean canHandle(CardRange o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        if (o.getAcsEndProtocolVersion() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsEndProtocolVersion");
        }

        if (o.getAcsStartProtocolVersion() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsStartProtocolVersion");
        }

        if (o.getEndRange() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "endRange");
        }

        if (o.getStartRange() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "startRange");
        }

        return ConstraintValidationResult.success();
    }
}
