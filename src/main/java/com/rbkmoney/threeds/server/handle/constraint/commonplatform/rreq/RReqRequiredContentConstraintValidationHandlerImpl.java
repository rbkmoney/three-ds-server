package com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.rbkmoney.threeds.server.dto.ConstraintType.AUTH_DEC_TIME_IS_EXPIRED;
import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class RReqRequiredContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    private final ChallengeFlowTransactionInfoStorageService challengeFlowTransactionInfoStorageService;

    @Override
    public boolean canHandle(RReq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        if (isBlank(o.getThreeDSServerTransID())) {
            return ConstraintValidationResult.failure(NOT_BLANK, "threeDSServerTransID");
        }

        ConstraintValidationResult validationResult =
                validateRequiredConditionField(o.getMessageCategory(), "messageCategory");
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!o.isRelevantMessageVersion()) {
            if (getValue(o.getTransStatusReason()) != null
                    && getValue(o.getTransStatusReason()).isReservedValueForNotRelevantMessageVersion()) {
                return ConstraintValidationResult.failure(PATTERN, "transStatusReason");
            }

            if (getValue(o.getChallengeCancel()) != null
                    && getValue(o.getChallengeCancel()).isReservedValueForNotRelevantMessageVersion()) {
                return ConstraintValidationResult.failure(PATTERN, "challengeCancel");
            }

            if (getValue(o.getAuthenticationMethod()) != null
                    && getValue(o.getAuthenticationMethod()).isReservedValueForNotRelevantMessageVersion()) {
                return ConstraintValidationResult.failure(PATTERN, "authenticationMethod");
            }
        }

        if (o.getAcsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "acsTransID");
        }

        if (o.getDsTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "dsTransID");
        }

        ChallengeFlowTransactionInfo transactionInfo =
                challengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(o.getThreeDSServerTransID());
        DeviceChannel deviceChannel = transactionInfo.getDeviceChannel();
        if (deviceChannel == DeviceChannel.APP_BASED
                && o.isRelevantMessageVersion()
                && o.getSdkTransID() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "sdkTransID");
        }

        MessageCategory messageCategory = getValue(o.getMessageCategory());
        if (messageCategory == MessageCategory.PAYMENT_AUTH) {
            validationResult = validateRequiredConditionField(o.getTransStatus(), "transStatus");
            if (!validationResult.isValid()) {
                return validationResult;
            }
        }

        LocalDateTime decoupledAuthMaxTime = transactionInfo.getDecoupledAuthMaxTime();
        AcsDecConInd acsDecConInd = transactionInfo.getAcsDecConInd();
        if (acsDecConInd == null) {
            if ((deviceChannel == DeviceChannel.APP_BASED || deviceChannel == DeviceChannel.BROWSER)
                    && o.getInteractionCounter() == null) {
                return ConstraintValidationResult.failure(NOT_NULL, "interactionCounter");
            }

            if (deviceChannel == DeviceChannel.APP_BASED
                    && o.getAcsRenderingType() == null) {
                return ConstraintValidationResult.failure(NOT_NULL, "acsRenderingType");
            }
        } else if (acsDecConInd == AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED) {
            if (decoupledAuthMaxTime.isBefore(LocalDateTime.now(Clock.systemUTC()))) {
                return ConstraintValidationResult.failure(AUTH_DEC_TIME_IS_EXPIRED,
                        "Timeout expiry reached for the transaction as defined in Section 5.5");
            }

            AuthenticationType authenticationType = getValue(o.getAuthenticationType());
            if (authenticationType != null && authenticationType != AuthenticationType.DECOUPLED) {
                return ConstraintValidationResult.failure(PATTERN, "authenticationType");
            }
        }

        o.setChallengeFlowTransactionInfo(transactionInfo);

        return ConstraintValidationResult.success();
    }
}
