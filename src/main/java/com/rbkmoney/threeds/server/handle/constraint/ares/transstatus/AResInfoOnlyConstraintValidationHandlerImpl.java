package com.rbkmoney.threeds.server.handle.constraint.ares.transstatus;

import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.INFORMATIONAL_ONLY;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;

@Component
@RequiredArgsConstructor
public class AResInfoOnlyConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return getEnumWrapperValue(o.getTransStatus()) == INFORMATIONAL_ONLY;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        AReq aReq = (AReq) o.getRequestMessage();
        ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd = aReq.getThreeDSRequestorChallengeInd();

        if (!(threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_RISK_ANALYSIS_PERFORMED
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_DATA_SHARE_ONLY
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED
                //todo for visa test (мб добавить условие на роутинг)
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.RESERVED_FOR_DS_USED_82)) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        return ConstraintValidationResult.success();
    }
}
