package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.transstatus;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.CHALLENGE_REQUIRED;
import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class AResChallengeRequiredConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return getValue(o.getTransStatus()) == CHALLENGE_REQUIRED;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd =
                ((AReq) o.getRequestMessage()).getThreeDSRequestorChallengeInd();
        DeviceChannel deviceChannel = ((AReq) o.getRequestMessage()).getDeviceChannel();

        if (deviceChannel == DeviceChannel.THREE_REQUESTOR_INITIATED) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        if (threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_DATA_SHARE_ONLY) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getAcsRenderingType() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsRenderingType");
        }

        if (deviceChannel == DeviceChannel.APP_BASED
                && o.getAcsSignedContent() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsSignedContent");
        }

        if (deviceChannel == DeviceChannel.BROWSER
                && o.getAcsURL() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsUrl");
        }

        ConstraintValidationResult validationResult =
                validateRequiredConditionField(o.getAcsChallengeMandated(), "acsChallengeMandated");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        return validateRequiredConditionField(o.getAuthenticationType(), "authenticationType");
    }
}
