package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.merchantcountrycode;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MerchantCountryCodeContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getMerchantCountryCode());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        return stringValidator.validateStringWithConstLength("merchantCountryCode", 3, o.getMerchantCountryCode());
    }
}
