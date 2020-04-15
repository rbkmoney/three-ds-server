package com.rbkmoney.threeds.server.handle.constraint.parq.threedsrequestordecreqind;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorDecReqInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;

@Component
@RequiredArgsConstructor
public class DecoupledPrefferedConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return getEnumWrapperValue(o.getThreeDSRequestorDecReqInd()) == ThreeDSRequestorDecReqInd.DECOUPLED_AUTH_IS_PREFFERED;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        if (o.getThreeDSRequestorDecMaxTime() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSRequestorDecMaxTime");
        }

        return ConstraintValidationResult.success();
    }
}
