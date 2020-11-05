package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.acctnumber;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.OUT_OF_CARD_RANGE;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestAcctNumberContentConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final DsProviderHolder dsProviderHolder;
    private final StringValidator stringValidator;
    private final CardRangesStorageService cardRangesStorageService;

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

        String tag = dsProviderHolder.getTag(o).orElseThrow();
        if (!cardRangesStorageService.isInCardRange(tag, acctNumber)) {
            return ConstraintValidationResult.failure(OUT_OF_CARD_RANGE, "acctNumber");
        }

        return ConstraintValidationResult.success();
    }
}
