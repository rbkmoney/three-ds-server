package com.rbkmoney.threeds.server.converter.commonplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@RequiredArgsConstructor
public class ErroWrapperToErroConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult source) {
        ErroWrapper erroWrapper = (ErroWrapper) source.getMessage();

        Erro errorRes = Erro.builder()
                .threeDSServerTransID(erroWrapper.getThreeDSServerTransID())
                .acsTransID(erroWrapper.getAcsTransID())
                .dsTransID(erroWrapper.getDsTransID())
                .errorCode(getValue(erroWrapper.getErrorCode()))
                .errorComponent(getValue(erroWrapper.getErrorComponent()))
                .errorDescription(erroWrapper.getErrorDescription())
                .errorDetail(erroWrapper.getErrorDetail())
                .errorMessageType(getValue(erroWrapper.getErrorMessageType()))
                .sdkTransID(erroWrapper.getSdkTransID())
                .notifyDsAboutError(false)
                .build();
        errorRes.setMessageVersion(erroWrapper.getMessageVersion());
        return errorRes;
    }
}
