package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class PResToPPrsConverter implements Converter<ValidationResult, Message> {

    private final EnvironmentMessageProperties messageProperties;
    private final CacheService cacheService;

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();

        List<CardRange> cardRangeData = Optional.ofNullable(pRes.getCardRangeData()).orElse(emptyList());

        if (((PReq) pRes.getRequestMessage()).getSerialNum() == null) {
            cardRangeData.forEach(cardRange -> cardRange.setActionInd(getActionIndEnumWrapper()));
        }

        if (pRes.getSerialNum() != null) {
            cacheService.saveSerialNum(pRes.getUlTestCaseId(), pRes.getSerialNum());
            cacheService.updateCardRanges(pRes.getUlTestCaseId(), cardRangeData);
        }

        PPrs pPrs = PPrs.builder()
                .p_messageVersion(getP_messageVersion(pRes))
                .p_completed(!isEmpty(cardRangeData))
                .messageExtension(getValue(pRes.getMessageExtension()))
                .build();
        pPrs.setMessageVersion(pRes.getMessageVersion());

        return pPrs;
    }

    private String getP_messageVersion(PRes pRes) {
        return Optional.ofNullable(pRes.getRequestMessage())
                .map(Message::getRequestMessage)
                .map(message -> (PPrq) message)
                .map(PPrq::getP_messageVersion)
                .orElse(messageProperties.getPMessageVersion());
    }

    private EnumWrapper<ActionInd> getActionIndEnumWrapper() {
        EnumWrapper<ActionInd> wrapper = new EnumWrapper<>();
        wrapper.setValue(ActionInd.ADD_CARD_RANGE_TO_CACHE);

        return wrapper;
    }
}
