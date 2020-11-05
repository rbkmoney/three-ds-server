package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.acsdecconind;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorDecReqInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class DecoupledAuthWillBeUsedConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return getValue(o.getAcsDecConInd()) == AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        ThreeDSRequestorDecReqInd threeDSRequestorDecReqInd = ((AReq) o.getRequestMessage()).getThreeDSRequestorDecReqInd();

        if (o.getCardholderInfo() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "cardholderInfo");
        }

        if (threeDSRequestorDecReqInd == ThreeDSRequestorDecReqInd.DO_NOT_USE_DECOUPLED_AUTH) {
            return ConstraintValidationResult.failure(PATTERN, "acsDecConInd");
        }

        return ConstraintValidationResult.success();
    }
}
