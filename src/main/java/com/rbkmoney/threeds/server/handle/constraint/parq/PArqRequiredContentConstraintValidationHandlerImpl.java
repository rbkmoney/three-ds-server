package com.rbkmoney.threeds.server.handle.constraint.parq;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class PArqRequiredContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        ConstraintValidationResult validationResult = validateRequiredConditionField(o.getDeviceChannel(), "deviceChannel");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        validationResult = validateRequiredConditionField(o.getMessageCategory(), "messageCategory");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        DeviceChannel deviceChannel = getValue(o.getDeviceChannel());
        MessageCategory messageCategory = getValue(o.getMessageCategory());

        if (deviceChannel == DeviceChannel.BROWSER) {
            validationResult = validateRequiredConditionField(o.getThreeDSCompInd(), "threeDSCompInd");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if ((deviceChannel == DeviceChannel.APP_BASED || deviceChannel == DeviceChannel.BROWSER)) {
            validationResult = validateRequiredConditionField(o.getThreeDSRequestorAuthenticationInd(), "threeDSRequestorAuthenticationInd");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (!o.isRelevantMessageVersion()) {
            if (getValue(o.getThreeRIInd()) != null
                    && getValue(o.getThreeRIInd()).isReservedValueForNotRelevantMessageVersion()) {
                return ConstraintValidationResult.failure(PATTERN, "threeRIInd");
            }

            if (getValue(o.getThreeDSRequestorChallengeInd()) != null
                    && getValue(o.getThreeDSRequestorChallengeInd()).isReservedValueForNotRelevantMessageVersion()) {
                return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorChallengeInd");
            }
        }

        if (o.getThreeDSRequestorID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSRequestorID");
        }

        if (o.getThreeDSRequestorName() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSRequestorName");
        }

        if (o.getThreeDSRequestorURL() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSRequestorURL");
        }

        if (deviceChannel == DeviceChannel.THREE_REQUESTOR_INITIATED) {
            validationResult = validateRequiredConditionField(o.getThreeRIInd(), "threeRIInd");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getAcquirerBIN() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acquirerBIN");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getAcquirerMerchantID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acquirerMerchantID");
        }

        if (deviceChannel == DeviceChannel.BROWSER
                && o.getBrowserAcceptHeader() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "browserAcceptHeader");
        }

        if (deviceChannel == DeviceChannel.BROWSER
                && o.isRelevantMessageVersion()
                && o.getBrowserJavascriptEnabled() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "browserJavascriptEnabled");
        }

        if (deviceChannel == DeviceChannel.BROWSER
                && o.getBrowserLanguage() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "browserLanguage");
        }

        if (deviceChannel == DeviceChannel.BROWSER
                && o.getBrowserUserAgent() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "browserUserAgent");
        }

        if (o.getAcctNumber() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acctNumber");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getDeviceRenderOptions() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "deviceRenderOptions");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getMcc() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "mcc");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getMerchantCountryCode() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "merchantCountryCode");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getMerchantName() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "merchantName");
        }

        if (deviceChannel == DeviceChannel.BROWSER
                && o.getNotificationURL() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "notificationURL");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getPurchaseAmount() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseAmount");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getPurchaseCurrency() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseCurrency");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getPurchaseExponent() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "purchaseExponent");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH) {
            validationResult = validateRequiredConditionField(o.getPurchaseDate(), "purchaseDate");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkAppID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkAppID");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkEncData() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkEncData");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkEphemPubKey() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkEphemPubKey");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkMaxTimeout() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkMaxTimeout");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkReferenceNumber() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkReferenceNumber");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkTransID");
        }

        return ConstraintValidationResult.success();
    }
}
