package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.address;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestBillAddressContentConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getBillingAddress() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        Address billingAddress = o.getBillingAddress();

        if (stringValidator.isNotNull(billingAddress.getAddrCity())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("billAddrCity", 50, billingAddress.getAddrCity());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(billingAddress.getAddrCountry())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithConstLength("billAddrCountry", 3, billingAddress.getAddrCountry());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(billingAddress.getAddrCountry())) {
                return ConstraintValidationResult.failure(PATTERN, "billAddrCountry");
            }
        }

        if (stringValidator.isNotNull(billingAddress.getAddrLine1())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("billAddrLine1", 50, billingAddress.getAddrLine1());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(billingAddress.getAddrLine2())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("billAddrLine2", 50, billingAddress.getAddrLine2());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(billingAddress.getAddrLine3())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("billAddrLine3", 50, billingAddress.getAddrLine3());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(billingAddress.getAddrPostCode())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("billAddrPostCode", 16, billingAddress.getAddrPostCode());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(billingAddress.getAddrState())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("billAddrState", 3, billingAddress.getAddrState());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (billingAddress.getAddrState() != null && billingAddress.getAddrCountry() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "billAddrCountry");
        }

        return ConstraintValidationResult.success();
    }
}
