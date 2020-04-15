package com.rbkmoney.threeds.server.handle.constraint.parq.threedsrequestorpriorauthenticationinfo;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorPriorAuthenticationInfoWrapper;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperGarbageValue;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getTemporalAccessorGarbageValue;

@Component
@RequiredArgsConstructor
public class ThreeDSReqPriorAuthInfoContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return o.getThreeDSRequestorPriorAuthenticationInfo() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
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

        if (getEnumWrapperGarbageValue(threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorAuthMethod()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorPriorAuthenticationInfo.threeDSReqPriorAuthMethod");
        }

        if (getTemporalAccessorGarbageValue(threeDSRequestorPriorAuthenticationInfo.getThreeDSReqPriorAuthTimestamp()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorPriorAuthenticationInfo.threeDSReqPriorAuthTimestamp");
        }

        return ConstraintValidationResult.success();
    }
}
