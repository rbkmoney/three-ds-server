package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getListWrapperValue;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class PResToPPrsConverter implements Converter<ValidationResult, Message> {

    private final EnvironmentProperties environmentProperties;
    private final CacheService cacheService;

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();
        List<CardRange> cardRangeData = Optional.ofNullable(pRes.getCardRangeData())
                .map(ListWrapper::getValue)
                .orElse(Collections.emptyList());

        if (((PReq) pRes.getRequestMessage()).getSerialNum() == null) {
            cardRangeData.forEach(
                    cardRange -> cardRange.setActionInd(getActionIndEnumWrapper())
            );
        }

        if (pRes.getSerialNum() != null) {
            cacheService.saveSerialNum(pRes.getXULTestCaseRunId(), pRes.getSerialNum());
            cacheService.updateCardRanges(pRes.getXULTestCaseRunId(), cardRangeData);
        }

        PPrs pPrs = PPrs.builder()
                .p_messageVersion(getP_messageVersion(pRes))
                .p_completed(!isEmpty(cardRangeData))
                .messageExtension(getListWrapperValue(pRes.getMessageExtension()))
                .build();
        pPrs.setMessageVersion(pRes.getMessageVersion());
        pPrs.setRequestMessage(pRes);
        return pPrs;
    }

    private String getP_messageVersion(PRes pRes) {
        return Optional.ofNullable(pRes.getRequestMessage())
                .map(Message::getRequestMessage)
                .map(message -> (PPrq) message)
                .map(PPrq::getP_messageVersion)
                .orElse(environmentProperties.getPMessageVersion());
    }

    private EnumWrapper<ActionInd> getActionIndEnumWrapper() {
        EnumWrapper<ActionInd> wrapper = new EnumWrapper<>();
        wrapper.setValue(ActionInd.ADD_CARD_RANGE_TO_CACHE);
        return wrapper;
    }
}
