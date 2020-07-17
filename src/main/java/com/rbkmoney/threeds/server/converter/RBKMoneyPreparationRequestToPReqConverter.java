package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestToPReqConverter implements Converter<ValidationResult, Message> {

    private final DsProviderHolder dsProviderHolder;
    private final IdGenerator idGenerator;
    private final EnvironmentMessageProperties messageProperties;

    @Override
    public Message convert(ValidationResult validationResult) {
        RBKMoneyPreparationRequest request = (RBKMoneyPreparationRequest) validationResult.getMessage();

        EnvironmentProperties environmentProperties = dsProviderHolder.getEnvironmentProperties();

        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(environmentProperties.getThreeDsServerRefNumber())
                .threeDSServerOperatorID(environmentProperties.getThreeDsServerOperatorId())
                .threeDSServerTransID(idGenerator.generateUUID())
                .messageExtension(emptyList())
                .serialNum(request.getSerialNum())
                .threeDSRequestorURL(environmentProperties.getThreeDsRequestorUrl())
                .build();
        pReq.setMessageVersion(messageProperties.getMessageVersion());

        return pReq;
    }
}
