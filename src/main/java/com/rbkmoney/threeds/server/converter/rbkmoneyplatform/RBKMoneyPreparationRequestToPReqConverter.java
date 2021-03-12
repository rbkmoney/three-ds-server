package com.rbkmoney.threeds.server.converter.rbkmoneyplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class RBKMoneyPreparationRequestToPReqConverter implements Converter<ValidationResult, Message> {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;
    private final IdGenerator idGenerator;

    @Override
    public Message convert(ValidationResult validationResult) {
        RBKMoneyPreparationRequest request = (RBKMoneyPreparationRequest) validationResult.getMessage();

        EnvironmentProperties environmentProperties = rbkMoneyDsProviderHolder.getEnvironmentProperties();

        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(environmentProperties.getThreeDsServerRefNumber())
                .threeDSServerOperatorID(environmentProperties.getThreeDsServerOperatorId())
                .threeDSServerTransID(idGenerator.generateUUID())
                .serialNum(request.getSerialNumber())
                .build();
        pReq.setMessageVersion(request.getMessageVersion());
        return pReq;
    }
}
