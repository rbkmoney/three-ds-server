package com.rbkmoney.threeds.server.handle.constraint.parq.browserjavascriptenabled;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class JSEnabledConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return o.getBrowserJavascriptEnabled() != null && o.getBrowserJavascriptEnabled();
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        DeviceChannel deviceChannel = getValue(o.getDeviceChannel());
        ConstraintValidationResult validationResult;

        if (deviceChannel == DeviceChannel.BROWSER
                && o.getBrowserJavaEnabled() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "browserJavaEnabled");
        }

        if (deviceChannel == DeviceChannel.BROWSER) {
            validationResult = validateRequiredConditionField(o.getBrowserColorDepth(), "browserColorDepth");
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (o.getBrowserScreenHeight() == null) {
                return ConstraintValidationResult.failure(NOT_NULL, "browserScreenHeight");
            }

            if (o.getBrowserScreenWidth() == null) {
                return ConstraintValidationResult.failure(NOT_NULL, "browserScreenWidth");
            }

            if (o.getBrowserTZ() == null) {
                return ConstraintValidationResult.failure(NOT_NULL, "browserTZ");
            }
        }

        return ConstraintValidationResult.success();
    }
}
