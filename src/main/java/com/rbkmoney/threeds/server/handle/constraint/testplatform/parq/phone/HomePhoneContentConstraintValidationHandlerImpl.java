package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.phone;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomePhoneContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final PhoneContentConstraintValidationHelper helper;

    @Override
    public boolean canHandle(PArq o) {
        return o.getHomePhone() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        return helper.validate(o.getHomePhone());
    }
}
