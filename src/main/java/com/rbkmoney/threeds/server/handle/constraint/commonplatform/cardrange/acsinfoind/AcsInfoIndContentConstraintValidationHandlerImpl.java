package com.rbkmoney.threeds.server.handle.constraint.commonplatform.cardrange.acsinfoind;

import com.rbkmoney.threeds.server.domain.acs.AcsInfoInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.cardrange.CardRangeConstraintValidationHandler;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class AcsInfoIndContentConstraintValidationHandlerImpl implements CardRangeConstraintValidationHandler {

    @Override
    public boolean canHandle(CardRange o) {
        return o.getAcsInfoInd() != null;
    }

    @Override
    public ConstraintValidationResult handle(CardRange o) {
        if (o.getAcsInfoInd().isGarbage()) {
            return ConstraintValidationResult.failure(PATTERN, "acsInfoInd");
        }

        List<EnumWrapper<AcsInfoInd>> acsInfoInds = o.getAcsInfoInd().getValue();
        if (acsInfoInds.isEmpty()) {
            return ConstraintValidationResult.failure(PATTERN, "acsInfoInd");
        }

        for (EnumWrapper<AcsInfoInd> acsInfoInd : acsInfoInds) {
            if (acsInfoInd.isGarbage()) {
                return ConstraintValidationResult.failure(PATTERN, "acsInfoInd");
            }

            if (acsInfoInd.getValue() == null) {
                return ConstraintValidationResult.failure(NOT_BLANK, "acsInfoInd");
            }
        }

        return ConstraintValidationResult.success();
    }
}
