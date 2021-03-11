package com.rbkmoney.threeds.server.converter.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.acs.AcsRenderingType;
import com.rbkmoney.threeds.server.domain.acs.AcsRenderingTypeWrapper;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationResponse;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:parametername", "checkstyle:localvariablename"})
public class AResToRBKMoneyAuthenticationResponseConverter implements Converter<ValidationResult, Message> {

    private final RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService;
    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;

    @Override
    public Message convert(ValidationResult validationResult) {
        ARes aRes = (ARes) validationResult.getMessage();

        TransactionStatus transStatus = getValue(aRes.getTransStatus());
        if (transStatus == TransactionStatus.CHALLENGE_REQUIRED_DECOUPLED_AUTH
                || transStatus == TransactionStatus.CHALLENGE_REQUIRED) {
            saveInStorageChallengeFlowTransactionInfo(aRes);
        }

        RBKMoneyAuthenticationResponse response = RBKMoneyAuthenticationResponse.builder()
                .threeDSServerTransID(aRes.getThreeDSServerTransID())
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
        response.setMessageVersion(aRes.getMessageVersion());
        return response;
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
                .dsProviderId(rbkMoneyDsProviderHolder.getDsProvider().orElseThrow())
                .messageVersion(aRes.getMessageVersion())
                .acsUrl(aRes.getAcsURL())
                .build();

        rbkMoneyChallengeFlowTransactionInfoStorageService
                .saveChallengeFlowTransactionInfo(threeDSServerTransID, transactionInfo);
    }
}
