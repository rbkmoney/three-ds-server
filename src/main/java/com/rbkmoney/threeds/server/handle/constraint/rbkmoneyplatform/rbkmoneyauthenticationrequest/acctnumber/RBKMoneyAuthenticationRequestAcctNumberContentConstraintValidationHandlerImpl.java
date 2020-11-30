package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.acctnumber;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.rbkmoney.threeds.server.dto.ConstraintType.OUT_OF_CARD_RANGE;

@Component
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestAcctNumberContentConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;
    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getAcctNumber() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        String acctNumber = o.getAcctNumber();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("acctNumber", 19, 13, acctNumber);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        Optional<String> dsProvider = rbkMoneyDsProviderHolder.getDsProvider();
        if (dsProvider.isEmpty()) {
            return ConstraintValidationResult.failure(OUT_OF_CARD_RANGE, "acctNumber");
        }

        return ConstraintValidationResult.success();
    }
}
