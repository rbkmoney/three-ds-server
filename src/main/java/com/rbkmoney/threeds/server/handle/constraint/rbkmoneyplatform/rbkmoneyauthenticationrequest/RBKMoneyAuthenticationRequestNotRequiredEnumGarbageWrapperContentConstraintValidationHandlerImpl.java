package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.INSTALMENT_TRANSACTION;
import static com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd.RECURRING_TRANSACTION;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl
        implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        DeviceChannel deviceChannel = getValue(o.getDeviceChannel());
        MessageCategory messageCategory = getValue(o.getMessageCategory());

        if (getGarbageValue(o.getAddrMatch()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "addrMatch");
        }

        if (!(o.getBrowserJavascriptEnabled() != null
                && o.getBrowserJavascriptEnabled()
                && deviceChannel == DeviceChannel.BROWSER)
                && getGarbageValue(o.getBrowserColorDepth()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "browserColorDepth");
        }

        Object purchaseDateGarbageValue = getGarbageValue(o.getPurchaseDate());
        if (!(getValue(o.getThreeRIInd()) == ThreeRIInd.INSTALMENT_TRANSACTION)) {
            if (messageCategory != MessageCategory.NON_PAYMENT_AUTH && purchaseDateGarbageValue != null) {
                return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
            }

            if (getGarbageValue(o.getRecurringExpiry()) != null) {
                return ConstraintValidationResult.failure(PATTERN, "recurringExpiry");
            }
        }

        ThreeDSRequestorAuthenticationInd authenticationInd = getValue(o.getThreeDSRequestorAuthenticationInd());
        if (!(authenticationInd == INSTALMENT_TRANSACTION || authenticationInd == RECURRING_TRANSACTION)) {
            if (messageCategory != MessageCategory.NON_PAYMENT_AUTH && purchaseDateGarbageValue != null) {
                return ConstraintValidationResult.failure(PATTERN, "purchaseDate");
            }

            if (getGarbageValue(o.getRecurringExpiry()) != null) {
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

        if (getValue(o.getWhiteListStatusSource()) == null && getGarbageValue(o.getWhiteListStatus()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatus");
        }

        if (getValue(o.getWhiteListStatus()) == null && getGarbageValue(o.getWhiteListStatusSource()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "whiteListStatusSource");
        }

        return ConstraintValidationResult.success();
    }
}
