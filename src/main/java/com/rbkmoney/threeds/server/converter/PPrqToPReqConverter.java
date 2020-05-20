package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getListWrapperValue;

@Component
@RequiredArgsConstructor
public class PPrqToPReqConverter implements Converter<ValidationResult, Message> {

    private final DirectoryServerProviderHolder providerHolder;
    private final CacheService cacheService;

    @Override
    public Message convert(ValidationResult validationResult) {
        PPrq pPrq = (PPrq) validationResult.getMessage();

        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(providerHolder.getEnvironmentProperties().getThreeDsServerRefNumber())
                .threeDSServerOperatorID(pPrq.getThreeDSServerOperatorID())
                .threeDSServerTransID(pPrq.getThreeDSServerTransID())
                .messageExtension(getListWrapperValue(pPrq.getMessageExtension()))
                .serialNum(cacheService.getSerialNum(pPrq.getXULTestCaseRunId()))
                .threeDSRequestorURL(pPrq.getThreeDSRequestorURL())
                .build();
        pReq.setMessageVersion(pPrq.getMessageVersion());
        pReq.setRequestMessage(pPrq);

        cacheService.clearSerialNum(pPrq.getXULTestCaseRunId());

        return pReq;
    }
}
