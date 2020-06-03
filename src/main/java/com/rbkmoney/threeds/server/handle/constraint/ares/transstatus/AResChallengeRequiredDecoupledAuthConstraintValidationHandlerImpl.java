package com.rbkmoney.threeds.server.handle.constraint.ares.transstatus;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorDecReqInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED_AUTH;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class AResChallengeRequiredDecoupledAuthConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return getValue(o.getTransStatus()) == CHALLENGE_REQUIRED_DECOUPLED_AUTH;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        AReq aReq = (AReq) o.getRequestMessage();
        ThreeDSRequestorDecReqInd threeDSRequestorDecReqInd = aReq.getThreeDSRequestorDecReqInd();

        if (threeDSRequestorDecReqInd != ThreeDSRequestorDecReqInd.DECOUPLED_AUTH_IS_PREFFERED) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        ConstraintValidationResult validationResult = validateRequiredConditionField(o.getAcsChallengeMandated(), "acsChallengeMandated");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        validationResult = validateRequiredConditionField(o.getAcsDecConInd(), "acsDecConInd");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (getValue(o.getAcsDecConInd()) == AcsDecConInd.DECOUPLED_AUTH_WILL_NOT_BE_USED) {
            return ConstraintValidationResult.failure(PATTERN, "acsDecConInd");
        }

        validationResult = validateRequiredConditionField(o.getAuthenticationType(), "authenticationType");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (getValue(o.getAuthenticationType()) != AuthenticationType.DECOUPLED) {
            return ConstraintValidationResult.failure(PATTERN, "authenticationType");
        }

        return ConstraintValidationResult.success();
    }
}
