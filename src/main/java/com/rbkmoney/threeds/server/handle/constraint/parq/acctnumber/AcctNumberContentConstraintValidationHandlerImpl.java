package com.rbkmoney.threeds.server.handle.constraint.parq.acctnumber;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.OUT_OF_CARD_RANGE;

@Component
@RequiredArgsConstructor
public class AcctNumberContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final DsProviderHolder dsProviderHolder;
    private final StringValidator stringValidator;
    private final CacheService cacheService;

    @Override
    public boolean canHandle(PArq o) {
        return o.getAcctNumber() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String acctNumber = o.getAcctNumber();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("acctNumber", 19, 13, acctNumber);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!cacheService.isInCardRange(dsProviderHolder.getTag(o), acctNumber)) {
            return ConstraintValidationResult.failure(OUT_OF_CARD_RANGE, "acctNumber");
        }

        return ConstraintValidationResult.success();
    }
}
