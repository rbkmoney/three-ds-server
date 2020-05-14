package com.rbkmoney.threeds.server.handle.constraint.parq.acctnumber;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.OUT_OF_CARD_RANGE;

@Component
@RequiredArgsConstructor
public class AcctNumberContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final DirectoryServerProviderHolder providerHolder;
    private final StringValidator stringValidator;
    private final CacheService configurableCacheService;

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

        String tag = providerHolder.getProvider() == DirectoryServerProvider.TEST
                ? o.getXULTestCaseRunId()
                : providerHolder.getProvider().getTag();

        if (!configurableCacheService.isInCardRange(tag, acctNumber)) {
            return ConstraintValidationResult.failure(OUT_OF_CARD_RANGE, "acctNumber");
        }

        return ConstraintValidationResult.success();
    }
}
