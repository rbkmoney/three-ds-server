package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneypreparationrequest;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;

@Component
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestRequiredContentConstraintValidationHandlerImpl
        implements RBKMoneyPreparationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyPreparationRequest o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyPreparationRequest o) {
        if (o.getProviderId() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "providerId");
        }

        return ConstraintValidationResult.success();
    }
}
