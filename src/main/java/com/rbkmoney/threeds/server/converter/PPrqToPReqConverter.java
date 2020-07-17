package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@RequiredArgsConstructor
public class PPrqToPReqConverter implements Converter<ValidationResult, Message> {

    private final DsProviderHolder dsProviderHolder;
    private final CacheService cacheService;

    @Override
    public Message convert(ValidationResult validationResult) {
        PPrq pPrq = (PPrq) validationResult.getMessage();

        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(dsProviderHolder.getEnvironmentProperties().getThreeDsServerRefNumber())
                .threeDSServerOperatorID(pPrq.getThreeDSServerOperatorID())
                .threeDSServerTransID(pPrq.getThreeDSServerTransID())
                .messageExtension(getValue(pPrq.getMessageExtension()))
                .serialNum(cacheService.getSerialNum(pPrq.getUlTestCaseId()))
                .threeDSRequestorURL(pPrq.getThreeDSRequestorURL())
                .build();
        pReq.setMessageVersion(pPrq.getMessageVersion());
        pReq.setUlTestCaseId(pPrq.getUlTestCaseId());

        cacheService.clearSerialNum(pPrq.getUlTestCaseId());

        return pReq;
    }
}
