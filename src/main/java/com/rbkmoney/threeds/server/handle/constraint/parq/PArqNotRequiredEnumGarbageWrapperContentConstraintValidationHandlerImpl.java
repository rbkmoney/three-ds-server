package com.rbkmoney.threeds.server.handle.constraint.parq;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.INSTALMENT_TRANSACTION;
import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.RECURRING_TRANSACTION;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.*;

@Component
@RequiredArgsConstructor
public class PArqNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        DeviceChannel deviceChannel = getEnumWrapperValue(o.getDeviceChannel());
        MessageCategory messageCategory = getEnumWrapperValue(o.getMessageCategory());

        if (getEnumWrapperGarbageValue(o.getAddrMatch()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "addrMatch");
        }

        if (!(o.getBrowserJavascriptEnabled() != null && o.getBrowserJavascriptEnabled() && deviceChannel == DeviceChannel.BROWSER)
                && getEnumWrapperGarbageValue(o.getBrowserColorDepth()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "browserColorDepth");
        }

        Object purchaseDateGarbageValue = getTemporalAccessorGarbageValue(o.getPurchaseDate());
        if (!(getEnumWrapperValue(o.getThreeRIInd()) == ThreeRIInd.INSTALMENT_TRANSACTION)) {
            if (messageCategory != MessageCategory.NON_PAYMENT_AUTH && purchaseDateGarbageValue != null) {
                return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
            }

            if (getTemporalAccessorGarbageValue(o.getRecurringExpiry()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "recurringExpiry");
            }
        }

        ThreeDSRequestorAuthenticationInd authenticationInd = getEnumWrapperValue(o.getThreeDSRequestorAuthenticationInd());
        if (!(authenticationInd == INSTALMENT_TRANSACTION || authenticationInd == RECURRING_TRANSACTION)) {
            if (messageCategory != MessageCategory.NON_PAYMENT_AUTH && purchaseDateGarbageValue != null) {
                return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
            }

            if (getTemporalAccessorGarbageValue(o.getRecurringExpiry()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "recurringExpiry");
            }
        }

        if (messageCategory != MessageCategory.PAYMENT_AUTH && purchaseDateGarbageValue != null) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
        }

        if (getEnumWrapperGarbageValue(o.getTransType()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transType");
        }

        if (getEnumWrapperGarbageValue(o.getAcctType()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctType");
        }

        if (getEnumWrapperGarbageValue(o.getThreeDSRequestorChallengeInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorChallengeInd");
        }

        if (getEnumWrapperGarbageValue(o.getThreeDSRequestorAuthenticationInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorAuthenticationInd");
        }

        if (deviceChannel != DeviceChannel.THREE_REQUESTOR_INITIATED
                && getEnumWrapperGarbageValue(o.getThreeRIInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeRIInd");
        }

        if (deviceChannel != DeviceChannel.BROWSER
                && getEnumWrapperGarbageValue(o.getThreeDSCompInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSCompInd");
        }

        if (getEnumWrapperGarbageValue(o.getThreeDSRequestorDecReqInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorDecReqInd");
        }

        if (o.getPayTokenInd() == null && getEnumWrapperGarbageValue(o.getPayTokenSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "payTokenSource");
        }

        if (getEnumWrapperValue(o.getWhiteListStatusSource()) == null && getEnumWrapperGarbageValue(o.getWhiteListStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        if (getEnumWrapperValue(o.getWhiteListStatus()) == null && getEnumWrapperGarbageValue(o.getWhiteListStatusSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatusSource");
        }

        return ConstraintValidationResult.success();
    }
}
