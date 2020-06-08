package com.rbkmoney.threeds.server.handle.constraint.parq.acctinfo;

import com.rbkmoney.threeds.server.domain.account.AccountInfoWrapper;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;

@Component
@RequiredArgsConstructor
public class AcctInfoContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return o.getAcctInfo() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        AccountInfoWrapper acctInfo = o.getAcctInfo();

        if (stringValidator.isNotNull(acctInfo.getNbPurchaseAccount())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("acctInfo.nbPurchaseAccount", 4, acctInfo.getNbPurchaseAccount());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        } else {
            return ConstraintValidationResult.failure(NOT_NULL, "acctInfo.nbPurchaseAccount");
        }

        if (stringValidator.isNotNull(acctInfo.getProvisionAttemptsDay())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("acctInfo.provisionAttemptsDay", 3, acctInfo.getProvisionAttemptsDay());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        } else {
            return ConstraintValidationResult.failure(NOT_NULL, "acctInfo.provisionAttemptsDay");
        }

        if (stringValidator.isNotNull(acctInfo.getTxnActivityDay())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("acctInfo.txnActivityDay", 3, acctInfo.getTxnActivityDay());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        } else {
            return ConstraintValidationResult.failure(NOT_NULL, "acctInfo.txnActivityDay");
        }

        if (stringValidator.isNotNull(acctInfo.getTxnActivityYear())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("acctInfo.txnActivityYear", 3, acctInfo.getTxnActivityYear());
            if (!validationResult.isValid()) {
                return validationResult;
            }
        } else {
            return ConstraintValidationResult.failure(NOT_NULL, "acctInfo.txnActivityYear");
        }

        if (getGarbageValue(acctInfo.getChAccAgeInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.chAccAgeInd");
        }

        if (getGarbageValue(acctInfo.getChAccChange()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.chAccChange");
        }

        if (getGarbageValue(acctInfo.getChAccChangeInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.chAccChangeInd");
        }

        if (getGarbageValue(acctInfo.getChAccDate()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.chAccDate");
        }

        if (getGarbageValue(acctInfo.getChAccPwChange()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.chAccPwChange");
        }

        if (getGarbageValue(acctInfo.getChAccPwChangeInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.chAccPwChangeInd");
        }

        if (getGarbageValue(acctInfo.getPaymentAccAge()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.paymentAccAge");
        }

        if (getGarbageValue(acctInfo.getPaymentAccInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.paymentAccInd");
        }

        if (getGarbageValue(acctInfo.getShipAddressUsage()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.shipAddressUsage");
        }

        if (getGarbageValue(acctInfo.getShipAddressUsageInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.shipAddressUsageInd");
        }

        if (getGarbageValue(acctInfo.getShipNameIndicator()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.shipNameIndicator");
        }

        if (getGarbageValue(acctInfo.getSuspiciousAccActivity()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctInfo.suspiciousAccActivity");
        }

        return ConstraintValidationResult.success();
    }
}
