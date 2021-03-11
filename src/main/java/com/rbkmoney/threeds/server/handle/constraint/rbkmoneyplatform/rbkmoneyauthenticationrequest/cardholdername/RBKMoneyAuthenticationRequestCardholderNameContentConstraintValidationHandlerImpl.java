package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.cardholdername;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestCardholderNameContentConstraintValidationHandlerImpl
        implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return stringValidator.isNotNull(o.getCardholderName());
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        return stringValidator.validateStringWithMinAndMaxLength("cardholderName", 45, 2, o.getCardholderName());
    }
}
