package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.whiteliststatussource;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestWhiteListStatusSourceContentConstraintValidationHandlerImpl
        implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return getValue(o.getWhiteListStatusSource()) != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        return validateRequiredConditionField(o.getWhiteListStatus(), "whiteListStatus");
    }
}
