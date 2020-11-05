package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.threedsrequestorauthenticationinfo;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInfoWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;

@Component
@RequiredArgsConstructor
public class ThreeDSReqAuthInfoContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return o.getThreeDSRequestorAuthenticationInfo() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        ThreeDSRequestorAuthenticationInfoWrapper threeDSRequestorAuthenticationInfo = o.getThreeDSRequestorAuthenticationInfo();

        if (stringValidator.isNotNull(threeDSRequestorAuthenticationInfo.getThreeDSReqAuthData())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("threeDSRequestorAuthenticationInfo.threeDSReqAuthData", 20000, threeDSRequestorAuthenticationInfo.getThreeDSReqAuthData());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        } else {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSRequestorAuthenticationInfo.threeDSReqAuthData");
        }

        if (getGarbageValue(threeDSRequestorAuthenticationInfo.getThreeDSReqAuthMethod()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorAuthenticationInfo.threeDSReqAuthMethod");
        }

        if (getGarbageValue(threeDSRequestorAuthenticationInfo.getThreeDSReqAuthTimestamp()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorAuthenticationInfo.threeDSReqAuthTimestamp");
        }

        return ConstraintValidationResult.success();
    }
}
