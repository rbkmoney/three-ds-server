package com.rbkmoney.threeds.server.handle.constraint.ares;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class AResRequiredContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        DeviceChannel deviceChannel = ((AReq) o.getRequestMessage()).getDeviceChannel();
        MessageCategory messageCategory = ((AReq) o.getRequestMessage()).getMessageCategory();

        if (!o.isRelevantMessageVersion() && getValue(o.getTransStatusReason()) != null
                && getValue(o.getTransStatusReason()).isReservedValueForNotRelevantMessageVersion()) {
            return ConstraintValidationResult.failure(PATTERN, "transStatusReason");
        }

        if (o.getThreeDSServerTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "threeDSServerTransID");
        }

        if (o.getAcsReferenceNumber() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsReferenceNumber");
        }

        if (o.getAcsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsTransID");
        }

        if (o.getDsReferenceNumber() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "dsReferenceNumber");
        }

        if (o.getDsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "dsTransID");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getSdkTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkTransID");
        }

        if (messageCategory == MessageCategory.PAYMENT_AUTH) {
            return validateRequiredConditionField(o.getTransStatus(), "transStatus");
        }

        return ConstraintValidationResult.success();
    }
}
