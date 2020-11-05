package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.threedsrequestordecreqind;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorDecReqInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestDecoupledPrefferedConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return getValue(o.getThreeDSRequestorDecReqInd()) == ThreeDSRequestorDecReqInd.DECOUPLED_AUTH_IS_PREFFERED;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        if (o.getThreeDSRequestorDecMaxTime() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSRequestorDecMaxTime");
        }

        return ConstraintValidationResult.success();
    }
}
