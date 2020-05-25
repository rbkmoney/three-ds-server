package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getListWrapperValue;

@Component
@RequiredArgsConstructor
public class PPrqToPReqConverter implements Converter<ValidationResult, Message> {

    private final EnvironmentProperties environmentProperties;
    private final CacheService cacheService;

    @Override
    public Message convert(ValidationResult validationResult) {
        PPrq pPrq = (PPrq) validationResult.getMessage();

        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(environmentProperties.getThreeDsServerRefNumber())
                .threeDSServerOperatorID(pPrq.getThreeDSServerOperatorID())
                .threeDSServerTransID(pPrq.getThreeDSServerTransID())
                .messageExtension(getListWrapperValue(pPrq.getMessageExtension()))
                .serialNum(cacheService.getSerialNum(pPrq.getXULTestCaseRunId()))
                .threeDSRequestorURL(pPrq.getThreeDSRequestorURL())
                .build();
        pReq.setMessageVersion(pPrq.getMessageVersion());
        pReq.setRequestMessage(pPrq);
        pReq.setXULTestCaseRunId(pPrq.getXULTestCaseRunId());

        cacheService.clearSerialNum(pPrq.getXULTestCaseRunId());

        return pReq;
    }
}
