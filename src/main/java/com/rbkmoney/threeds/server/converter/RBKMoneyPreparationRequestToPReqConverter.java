package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestToPReqConverter implements Converter<ValidationResult, Message> {

    private final DirectoryServerProviderHolder providerHolder;
    private final EnvironmentMessageProperties messageProperties;

    @Override
    public Message convert(ValidationResult validationResult) {
        RBKMoneyPreparationRequest request = (RBKMoneyPreparationRequest) validationResult.getMessage();

        // TODO [a.romanov]: operatorID, transID, requestorURL?
        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(providerHolder.getEnvironmentProperties().getThreeDsServerRefNumber())
//                .threeDSServerOperatorID(pPrq.getThreeDSServerOperatorID())
//                .threeDSServerTransID(pPrq.getThreeDSServerTransID())
                .messageExtension(emptyList())
                .serialNum(request.getSerialNum())
//                .threeDSRequestorURL(pPrq.getThreeDSRequestorURL())
                .build();
        pReq.setMessageVersion(messageProperties.getMessageVersion());
        pReq.setRequestMessage(request);

        return pReq;
    }
}
