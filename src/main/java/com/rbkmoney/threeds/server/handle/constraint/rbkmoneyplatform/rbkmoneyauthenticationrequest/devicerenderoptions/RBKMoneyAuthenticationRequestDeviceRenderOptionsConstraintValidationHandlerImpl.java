package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.devicerenderoptions;

import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptionsWrapper;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.sdk.SdkUiType;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestDeviceRenderOptionsConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getDeviceRenderOptions() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        DeviceRenderOptionsWrapper deviceRenderOptions = o.getDeviceRenderOptions();

        ConstraintValidationResult validationResult = validateRequiredConditionField(deviceRenderOptions.getSdkInterface(), "deviceRenderOptions.sdkInterface");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        ListWrapper<EnumWrapper<SdkUiType>> sdkUiType = deviceRenderOptions.getSdkUiType();
        if (sdkUiType.isGarbage() || sdkUiType.getValue().isEmpty()) {
            return ConstraintValidationResult.failure(PATTERN, "deviceRenderOptions.sdkUiType");
        }

        for (EnumWrapper<SdkUiType> sdkUiTypeEnumWrapper : sdkUiType.getValue()) {
            validationResult = validateRequiredConditionField(sdkUiTypeEnumWrapper, "deviceRenderOptions.sdkUiType");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        return ConstraintValidationResult.success();
    }
}
