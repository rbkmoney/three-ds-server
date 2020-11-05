package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.address;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class ShipAddressContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return o.getShippingAddress() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        Address shippingAddress = o.getShippingAddress();

        if (stringValidator.isNotNull(shippingAddress.getAddrCity())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrCity", 50, shippingAddress.getAddrCity());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(shippingAddress.getAddrCountry())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrCountry", 3, shippingAddress.getAddrCountry());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(shippingAddress.getAddrCountry())) {
                return ConstraintValidationResult.failure(PATTERN, "shipAddrCountry");
            }
        }

        if (stringValidator.isNotNull(shippingAddress.getAddrLine1())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrLine1", 50, shippingAddress.getAddrLine1());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(shippingAddress.getAddrLine2())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrLine2", 50, shippingAddress.getAddrLine2());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(shippingAddress.getAddrLine3())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrLine3", 50, shippingAddress.getAddrLine3());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(shippingAddress.getAddrPostCode())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrPostCode", 16, shippingAddress.getAddrPostCode());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (stringValidator.isNotNull(shippingAddress.getAddrState())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("shipAddrState", 3, shippingAddress.getAddrState());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (shippingAddress.getAddrState() != null && shippingAddress.getAddrCountry() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "shipAddrCountry");
        }

        return ConstraintValidationResult.success();
    }
}
