package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneygetchallengerequest;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;

@Component
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl
        implements RBKMoneyGetChallengeRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyGetChallengeRequest o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyGetChallengeRequest o) {
        if (getGarbageValue(o.getChallengeWindowSize()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "challengeWindowSize");
        }

        return ConstraintValidationResult.success();
    }
}
