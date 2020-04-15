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
public class WorkPhoneContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return o.getWorkPhone() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        Phone workPhone = o.getWorkPhone();

        if (stringValidator.isNotNull(workPhone.getCc())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("cc", 3, 1, workPhone.getCc());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(workPhone.getSubscriber())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("subscriber", 15, workPhone.getSubscriber());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        return ConstraintValidationResult.success();
    }
}
