package com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.transstatus;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED_AUTH;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class RReqChallengeRequiredDecoupledAuthConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return getValue(o.getTransStatus()) == CHALLENGE_REQUIRED_DECOUPLED_AUTH;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        return ConstraintValidationResult.failure(PATTERN, "transStatus");
    }
}
