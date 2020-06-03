package com.rbkmoney.threeds.server.handle.constraint.rbkgcq;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;

@Component
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestRequiredContentConstraintValidationHandlerImpl implements RBKMoneyGetChallengeRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyGetChallengeRequest o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyGetChallengeRequest o) {
        if (o.getThreeDSServerTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSServerTransID");
        }

        if (o.getAcsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsTransID");
        }

        return ConstraintValidationResult.success();
    }
}
