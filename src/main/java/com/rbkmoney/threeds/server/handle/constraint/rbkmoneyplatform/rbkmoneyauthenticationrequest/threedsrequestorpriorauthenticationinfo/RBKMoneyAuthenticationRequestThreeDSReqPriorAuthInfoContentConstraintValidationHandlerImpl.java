package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.threedsrequestorpriorauthenticationinfo;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorPriorAuthenticationInfoWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestThreeDSReqPriorAuthInfoContentConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getThreeDSRequestorPriorAuthenticationInfo() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        ThreeDSRequestorPriorAuthenticationInfoWrapper threeDSRequestorPriorAuthenticationInfo = o.getThreeDSRequestorPriorAuthenticationInfo();

        if (stringValidator.isNotNull(threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorAuthData())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("threeDSRequestorPriorAuthenticationInfo.threeDSReqPriorAuthData", 2048, threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorAuthData());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorRef())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("threeDSRequestorPriorAuthenticationInfo.threeDSReqPriorRef", 36, threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorRef());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (getGarbageValue(threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorAuthMethod()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorPriorAuthenticationInfo.threeDSReqPriorAuthMethod");
        }

        if (getGarbageValue(threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorAuthTimestamp()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorPriorAuthenticationInfo.threeDSReqPriorAuthTimestamp");
        }

        return ConstraintValidationResult.success();
    }
}
