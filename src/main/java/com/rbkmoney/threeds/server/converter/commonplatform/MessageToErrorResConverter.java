package com.rbkmoney.threeds.server.converter.commonplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.TransactionalMessage;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.MessageTypeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;
import java.util.function.Function;

import static com.rbkmoney.threeds.server.domain.error.ErrorComponent.THREE_DS_SERVER;
import static org.apache.commons.lang3.StringUtils.isBlank;

@RequiredArgsConstructor
public class MessageToErrorResConverter implements Converter<ValidationResult, Message> {

    private final EnvironmentMessageProperties environmentMessageProperties;

    @Override
    public Message convert(ValidationResult validationResult) {
        Message message = validationResult.getMessage();

        Erro error;
        ErrorCode errorCode = validationResult.getErrorCode();
        String errorDescription = validationResult.getErrorDescription();
        String errorDetail = validationResult.getErrorDetail();

        if (message != null) {
            Message requestMessage = message.getRequestMessage();

            error = Erro.builder()
                    .threeDSServerTransID(getNullableString(message.getThreeDSServerTransID(),
                            getRequestId(requestMessage, TransactionalMessage::getThreeDSServerTransID)))
                    .acsTransID(getNullableString(message.getAcsTransID(),
                            getRequestId(requestMessage, TransactionalMessage::getAcsTransID)))
                    .dsTransID(getNullableString(message.getDsTransID(),
                            getRequestId(requestMessage, TransactionalMessage::getDsTransID)))
                    .errorCode(errorCode)
                    .errorComponent(THREE_DS_SERVER)
                    .errorDescription(errorDescription)
                    .errorDetail(errorDetail)
                    .errorMessageType(MessageTypeUtils.resolve(message))
                    .sdkTransID(getNullableString(message.getSdkTransID(),
                            getRequestId(requestMessage, TransactionalMessage::getSdkTransID)))
                    .build();
            error.setMessageVersion(getMessageVersion(message, requestMessage));
        } else {
            error = Erro.builder()
                    .errorCode(errorCode)
                    .errorComponent(THREE_DS_SERVER)
                    .errorDescription(errorDescription)
                    .errorDetail(errorDetail)
                    .build();
            error.setMessageVersion(environmentMessageProperties.getMessageVersion());
        }
        error.setNotifyDsAboutError(isNotifyDsAboutError(errorCode));
        return error;
    }

    private boolean isNotifyDsAboutError(ErrorCode errorCode) {
        return !(errorCode == ErrorCode.TRANSACTION_TIMED_OUT_402
                || errorCode == ErrorCode.ACCESS_DENIED_OR_INVALID_ENDPOINT_303
                || errorCode == ErrorCode.TRANSIENT_SYSTEM_FAILURE_403
                || errorCode == ErrorCode.PERMANENT_SYSTEM_FAILURE_404
                || errorCode == ErrorCode.SYSTEM_CONNECTION_FAILURE_405);
    }

    private String getMessageVersion(Message message, Message requestMessage) {
        String messageVersion = getNullableString(
                message.getMessageVersion(),
                getMessageVersion(requestMessage, Message::getMessageVersion,
                        environmentMessageProperties.getMessageVersion()));

        return environmentMessageProperties.getValidMessageVersions().contains(messageVersion)
                ? messageVersion
                : environmentMessageProperties.getMessageVersion();
    }

    private String getMessageVersion(Message requestMessage, Function<Message, String> messageVersionFunction,
                                     String other) {
        return Optional.ofNullable(requestMessage).map(messageVersionFunction).orElse(other);
    }

    private String getRequestId(Message requestMessage, Function<Message, String> idFunction) {
        return Optional.ofNullable(requestMessage).map(idFunction).orElse(null);
    }

    private String getNullableString(String id, String requestId) {
        return isBlank(id) ? requestId : id;
    }
}
