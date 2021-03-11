package com.rbkmoney.threeds.server.converter.testplatform;

import com.rbkmoney.threeds.server.domain.acs.AcsRenderingType;
import com.rbkmoney.threeds.server.domain.acs.AcsRenderingTypeWrapper;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformChallengeFlowTransactionInfoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:parametername", "checkstyle:localvariablename"})
public class AResToPArsConverter implements Converter<ValidationResult, Message> {

    private final TestPlatformChallengeFlowTransactionInfoStorageService
            testPlatformChallengeFlowTransactionInfoStorageService;

    @Override
    public Message convert(ValidationResult validationResult) {
        ARes aRes = (ARes) validationResult.getMessage();

        TransactionStatus transStatus = getValue(aRes.getTransStatus());
        if (transStatus == TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED_AUTH
                || transStatus == TransactionStatus.CHALLENGE_REQUIRED) {
            saveInStorageChallengeFlowTransactionInfo(aRes);
        }

        PArs pArs = PArs.builder()
                .threeDSServerTransID(aRes.getThreeDSServerTransID())
                .p_messageVersion(getP_messageVersion(aRes))
                .transStatus(transStatus)
                .dsReferenceNumber(aRes.getDsReferenceNumber())
                .acsReferenceNumber(aRes.getAcsReferenceNumber())
                .acsTransID(aRes.getAcsTransID())
                .dsTransID(aRes.getDsTransID())
                .authenticationValue(aRes.getAuthenticationValue())
                .acsRenderingType(getAcsRenderingType(aRes))
                .acsOperatorID(aRes.getAcsOperatorID())
                .acsSignedContent(getAcsSignedContent(aRes))
                .acsURL(getAcsURL(aRes))
                .authenticationType(getValue(aRes.getAuthenticationType()))
                .acsChallengeMandated(getValue(aRes.getAcsChallengeMandated()))
                .eci(aRes.getEci())
                .messageExtension(getValue(aRes.getMessageExtension()))
                .sdkTransID(aRes.getSdkTransID())
                .transStatusReason(getValue(aRes.getTransStatusReason()))
                .cardholderInfo(aRes.getCardholderInfo())
                .broadInfo(aRes.getBroadInfo())
                .acsDecConInd(getValue(aRes.getAcsDecConInd()))
                .whiteListStatus(getValue(aRes.getWhiteListStatus()))
                .whiteListStatusSource(getValue(aRes.getWhiteListStatusSource()))
                .build();
        pArs.setMessageVersion(aRes.getMessageVersion());
        pArs.setUlTestCaseId(aRes.getRequestMessage().getUlTestCaseId());
        return pArs;
    }

    private String getP_messageVersion(ARes aRes) {
        return Optional.ofNullable(aRes.getRequestMessage())
                .map(Message::getRequestMessage)
                .map(message -> (PArq) message)
                .map(PArq::getP_messageVersion)
                .orElse("1.0.5");
    }

    private String getAcsURL(ARes aRes) {
        DeviceChannel deviceChannel = ((AReq) aRes.getRequestMessage()).getDeviceChannel();
        if (deviceChannel != DeviceChannel.BROWSER) {
            return null;
        }

        return aRes.getAcsURL();
    }

    private String getAcsSignedContent(ARes aRes) {
        DeviceChannel deviceChannel = ((AReq) aRes.getRequestMessage()).getDeviceChannel();
        if (deviceChannel != DeviceChannel.APP_BASED) {
            return null;
        }

        return aRes.getAcsSignedContent();
    }

    private AcsRenderingType getAcsRenderingType(ARes aRes) {
        Optional<AcsRenderingTypeWrapper> optional = Optional.ofNullable(aRes.getAcsRenderingType());

        if (optional.isPresent()) {
            AcsRenderingType acsRenderingType = new AcsRenderingType();
            acsRenderingType.setAcsInterface(
                    optional.map(AcsRenderingTypeWrapper::getAcsInterface).map(EnumWrapper::getValue).orElse(null));
            acsRenderingType.setAcsUiTemplate(
                    optional.map(AcsRenderingTypeWrapper::getAcsUiTemplate).map(EnumWrapper::getValue).orElse(null));
            return acsRenderingType;
        } else {
            return null;
        }
    }

    private void saveInStorageChallengeFlowTransactionInfo(ARes aRes) {
        AReq aReq = (AReq) aRes.getRequestMessage();

        String threeDSServerTransID = aRes.getThreeDSServerTransID();

        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .threeDsServerTransId(threeDSServerTransID)
                .deviceChannel(aReq.getDeviceChannel())
                .decoupledAuthMaxTime(aReq.getDecoupledAuthMaxTime())
                .acsDecConInd(getValue(aRes.getAcsDecConInd()))
                .messageVersion(aRes.getMessageVersion())
                .acsUrl(aRes.getAcsURL())
                .build();

        testPlatformChallengeFlowTransactionInfoStorageService
                .saveChallengeFlowTransactionInfo(threeDSServerTransID, transactionInfo);
    }
}
