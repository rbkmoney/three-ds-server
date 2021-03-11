package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.whiteliststatus;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestWhiteListStatusContentConstraintValidationHandlerImpl
        implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return getValue(o.getWhiteListStatus()) != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        ConstraintValidationResult validationResult =
                validateRequiredConditionField(o.getWhiteListStatus(), "whiteListStatus");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (getValue(o.getWhiteListStatus()) != WhiteListStatus.WHITELISTED
                && getValue(o.getWhiteListStatus()) != WhiteListStatus.NOT_WHITELISTED) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        return ConstraintValidationResult.success();
    }
}
