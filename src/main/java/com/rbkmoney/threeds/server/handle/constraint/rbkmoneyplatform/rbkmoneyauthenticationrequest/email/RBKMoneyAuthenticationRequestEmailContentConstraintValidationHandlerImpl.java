package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.email;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestEmailContentConstraintValidationHandlerImpl
        implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getEmail() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        if (!emailValidator.isValid(o.getEmail())) {
            return ConstraintValidationResult.failure(PATTERN, "email");
        }

        return ConstraintValidationResult.success();
    }
}
