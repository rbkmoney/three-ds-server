package com.rbkmoney.threeds.server.converter.testplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
public class PResToPPrsConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();

        PPrs pPrs = PPrs.builder()
                .p_messageVersion(getP_messageVersion(pRes))
                .p_completed(!isEmpty(pRes.getCardRangeData()))
                .messageExtension(getValue(pRes.getMessageExtension()))
                .build();
        pPrs.setMessageVersion(pRes.getMessageVersion());
        pPrs.setUlTestCaseId(pRes.getRequestMessage().getUlTestCaseId());
        return pPrs;
    }

    private String getP_messageVersion(PRes pRes) {
        return Optional.ofNullable(pRes.getRequestMessage())
                .map(Message::getRequestMessage)
                .map(message -> (PPrq) message)
                .map(PPrq::getP_messageVersion)
                .orElse("1.0.5");
    }
}
