package com.rbkmoney.threeds.server.converter.commonplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("CheckStyle")
@RequiredArgsConstructor
public class PReqToFixedPReqConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult validationResult) {
        PReq pReq = (PReq) validationResult.getMessage();

        PReq pReqFixed = PReq.builder()
                .threeDSServerRefNumber(pReq.getThreeDSServerRefNumber())
                .threeDSServerOperatorID(pReq.getThreeDSServerOperatorID())
                .threeDSServerTransID(pReq.getThreeDSServerTransID())
                .messageExtension(pReq.getMessageExtension())
                .serialNum(null)
                .threeDSRequestorURL(pReq.getThreeDSRequestorURL())
                .build();
        pReqFixed.setMessageVersion(pReq.getMessageVersion());
        pReqFixed.setRequestMessage(pReq.getRequestMessage());
        return pReqFixed;
    }
}
