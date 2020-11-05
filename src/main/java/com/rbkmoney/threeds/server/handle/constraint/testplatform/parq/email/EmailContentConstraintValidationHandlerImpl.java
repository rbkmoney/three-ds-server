package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.email;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class EmailContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean canHandle(PArq o) {
        return o.getEmail() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        if (!emailValidator.isValid(o.getEmail())) {
            return ConstraintValidationResult.failure(PATTERN, "email");
        }

        return ConstraintValidationResult.success();
    }
}
