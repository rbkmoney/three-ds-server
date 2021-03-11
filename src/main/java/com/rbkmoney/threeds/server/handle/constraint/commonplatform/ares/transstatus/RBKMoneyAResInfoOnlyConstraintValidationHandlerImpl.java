package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.transstatus;

import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.INFORMATIONAL_ONLY;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:localvariablename"})
public class RBKMoneyAResInfoOnlyConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;

    @Override
    public boolean canHandle(ARes o) {
        return getValue(o.getTransStatus()) == INFORMATIONAL_ONLY;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        AReq aReq = (AReq) o.getRequestMessage();
        ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd = aReq.getThreeDSRequestorChallengeInd();

        if (!(isSatisfactoryChallengeIndForTransStatus(threeDSRequestorChallengeInd)
                || (threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.RESERVED_FOR_DS_USED_82
                && rbkMoneyDsProviderHolder.getDsProvider().orElseThrow().equals(DsProvider.VISA.getId())))) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        return ConstraintValidationResult.success();
    }

    private boolean isSatisfactoryChallengeIndForTransStatus(
            ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd) {
        return threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_RISK_ANALYSIS_PERFORMED
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_DATA_SHARE_ONLY
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED;
    }
}
