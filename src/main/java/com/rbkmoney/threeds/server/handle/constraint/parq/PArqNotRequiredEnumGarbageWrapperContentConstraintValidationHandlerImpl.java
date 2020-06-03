package com.rbkmoney.threeds.server.handle.constraint.parq;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.utils.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.INSTALMENT_TRANSACTION;
import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.RECURRING_TRANSACTION;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;

@Component
@RequiredArgsConstructor
public class PArqNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        DeviceChannel deviceChannel = Wrappers.getValue(o.getDeviceChannel());
        MessageCategory messageCategory = Wrappers.getValue(o.getMessageCategory());

        if (getGarbageValue(o.getAddrMatch()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "addrMatch");
        }

        if (!(o.getBrowserJavascriptEnabled() != null && o.getBrowserJavascriptEnabled() && deviceChannel == DeviceChannel.BROWSER)
                && getGarbageValue(o.getBrowserColorDepth()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "browserColorDepth");
        }

        Object purchaseDateGarbageValue = Wrappers.getGarbageValue(o.getPurchaseDate());
        if (!(Wrappers.getValue(o.getThreeRIInd()) == ThreeRIInd.INSTALMENT_TRANSACTION)) {
            if (messageCategory != MessageCategory.NON_PAYMENT_AUTH && purchaseDateGarbageValue != null) {
                return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
            }

            if (Wrappers.getGarbageValue(o.getRecurringExpiry()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "recurringExpiry");
            }
        }

        ThreeDSRequestorAuthenticationInd authenticationInd = Wrappers.getValue(o.getThreeDSRequestorAuthenticationInd());
        if (!(authenticationInd == INSTALMENT_TRANSACTION || authenticationInd == RECURRING_TRANSACTION)) {
            if (messageCategory != MessageCategory.NON_PAYMENT_AUTH && purchaseDateGarbageValue != null) {
                return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
            }

            if (Wrappers.getGarbageValue(o.getRecurringExpiry()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "recurringExpiry");
            }
        }

        if (messageCategory != MessageCategory.PAYMENT_AUTH && purchaseDateGarbageValue != null) {
            return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
        }

        if (getGarbageValue(o.getTransType()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "transType");
        }

        if (getGarbageValue(o.getAcctType()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "acctType");
        }

        if (getGarbageValue(o.getThreeDSRequestorChallengeInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorChallengeInd");
        }

        if (getGarbageValue(o.getThreeDSRequestorAuthenticationInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorAuthenticationInd");
        }

        if (deviceChannel != DeviceChannel.THREE_REQUESTOR_INITIATED
                && getGarbageValue(o.getThreeRIInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeRIInd");
        }

        if (deviceChannel != DeviceChannel.BROWSER
                && getGarbageValue(o.getThreeDSCompInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSCompInd");
        }

        if (getGarbageValue(o.getThreeDSRequestorDecReqInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorDecReqInd");
        }

        if (o.getPayTokenInd() == null && getGarbageValue(o.getPayTokenSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "payTokenSource");
        }

        if (Wrappers.getValue(o.getWhiteListStatusSource()) == null && getGarbageValue(o.getWhiteListStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        if (Wrappers.getValue(o.getWhiteListStatus()) == null && getGarbageValue(o.getWhiteListStatusSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatusSource");
        }

        return ConstraintValidationResult.success();
    }
}
