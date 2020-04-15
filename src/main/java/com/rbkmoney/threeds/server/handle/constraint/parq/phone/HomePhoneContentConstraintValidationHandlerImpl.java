package com.rbkmoney.threeds.server.handle.constraint.parq.phone;

import com.rbkmoney.threeds.server.domain.Phone;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomePhoneContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return o.getHomePhone() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        Phone homePhone = o.getHomePhone();

        if (stringValidator.isNotNull(homePhone.getCc())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("cc", 3, 1, homePhone.getCc());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(homePhone.getSubscriber())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("subscriber", 15, homePhone.getSubscriber());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        return ConstraintValidationResult.success();
    }
}
