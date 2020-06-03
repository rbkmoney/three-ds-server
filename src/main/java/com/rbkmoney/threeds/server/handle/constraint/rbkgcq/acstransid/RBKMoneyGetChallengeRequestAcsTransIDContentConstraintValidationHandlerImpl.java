package com.rbkmoney.threeds.server.handle.constraint.rbkgcq.acstransid;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkgcq.RBKMoneyGetChallengeRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestAcsTransIDContentConstraintValidationHandlerImpl implements RBKMoneyGetChallengeRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyGetChallengeRequest o) {
        return stringValidator.isNotNull(o.getAcsTransID());
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyGetChallengeRequest o) {
        return stringValidator.validateUUID("acsTransID", o.getAcsTransID());
    }
}
