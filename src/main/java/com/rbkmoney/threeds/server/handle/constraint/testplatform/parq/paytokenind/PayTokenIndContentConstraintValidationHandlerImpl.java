package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.paytokenind;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class PayTokenIndContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return o.getPayTokenInd() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        if (!o.getPayTokenInd()) {
            return ConstraintValidationResult.failure(PATTERN, "payTokenInd");
        }

        if (o.isRelevantMessageVersion()) {
            return validateRequiredConditionField(o.getPayTokenSource(), "payTokenSource");
        }

        return ConstraintValidationResult.success();
    }
}
