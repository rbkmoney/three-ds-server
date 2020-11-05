package com.rbkmoney.threeds.server.converter.rbkmoneyplatform;

import com.rbkmoney.threeds.server.converter.thrift.CardRangeConverter;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationResponse;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.utils.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.ADD_CARD_RANGE_TO_CACHE;

@RequiredArgsConstructor
public class PResToRBKMoneyPreparationResponseConverter implements Converter<ValidationResult, Message> {

    private final DsProviderHolder dsProviderHolder;
    private final CardRangeConverter cardRangeConverter;

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();

        List<CardRange> cardRangeData = Collections.safeList(pRes.getCardRangeData());

        cardRangeData.forEach(this::fillEmptyActionInd);

        var rbkMoneyCardRanges = new ArrayList<com.rbkmoney.threeds.server.domain.rbkmoney.cardrange.RBKMoneyCardRange>();

        Iterator<CardRange> iterator = cardRangeData.iterator();
        while (iterator.hasNext()) {
            CardRange cardRange = iterator.next();
            var rbkMoneyCardRange = cardRangeConverter.toRBKMoney(cardRange);
            rbkMoneyCardRanges.add(rbkMoneyCardRange);
            iterator.remove();
        }

        return RBKMoneyPreparationResponse.builder()
                .providerId(dsProviderHolder.getTag(pRes).orElseThrow())
                .serialNum(pRes.getSerialNum())
                .cardRanges(rbkMoneyCardRanges)
                .build();
    }

    private void fillEmptyActionInd(CardRange cardRange) {
        if (cardRange.getActionInd() == null) {
            EnumWrapper<ActionInd> addAction = new EnumWrapper<>();
            addAction.setValue(ADD_CARD_RANGE_TO_CACHE);

            cardRange.setActionInd(addAction);
        }
    }

}
