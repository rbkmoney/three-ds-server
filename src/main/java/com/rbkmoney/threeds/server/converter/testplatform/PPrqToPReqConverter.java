package com.rbkmoney.threeds.server.converter.testplatform;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@SuppressWarnings("CheckStyle")
@RequiredArgsConstructor
public class PPrqToPReqConverter implements Converter<ValidationResult, Message> {

    private final EnvironmentProperties environmentProperties;
    private final TestPlatformSerialNumStorageService testPlatformSerialNumStorageService;

    @Override
    public Message convert(ValidationResult validationResult) {
        PPrq pPrq = (PPrq) validationResult.getMessage();

        PReq pReq = PReq.builder()
                .threeDSServerRefNumber(environmentProperties.getThreeDsServerRefNumber())
                .threeDSServerOperatorID(pPrq.getThreeDSServerOperatorID())
                .threeDSServerTransID(pPrq.getThreeDSServerTransID())
                .messageExtension(getValue(pPrq.getMessageExtension()))
                .serialNum(testPlatformSerialNumStorageService.getSerialNum(pPrq.getUlTestCaseId()))
                .threeDSRequestorURL(pPrq.getThreeDSRequestorURL())
                .build();
        pReq.setMessageVersion(pPrq.getMessageVersion());
        pReq.setUlTestCaseId(pPrq.getUlTestCaseId());
        return pReq;
    }
}
