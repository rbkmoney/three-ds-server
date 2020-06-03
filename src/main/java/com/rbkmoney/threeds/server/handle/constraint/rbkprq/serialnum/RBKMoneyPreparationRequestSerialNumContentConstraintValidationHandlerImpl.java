package com.rbkmoney.threeds.server.handle.constraint.rbkprq.serialnum;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkprq.RBKMoneyPreparationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestSerialNumContentConstraintValidationHandlerImpl implements RBKMoneyPreparationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyPreparationRequest o) {
        return o.getSerialNum() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyPreparationRequest o) {
        String candidate = o.getSerialNum();
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(PATTERN, "serialNum");
        }

        if (candidate.length() > 20) {
            return ConstraintValidationResult.failure(PATTERN, "serialNum");
        }

        return ConstraintValidationResult.success();
    }
}
