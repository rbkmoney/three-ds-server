package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationResponse;
import com.rbkmoney.threeds.server.dto.CardRangeDTO;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.exeption.NullPointerActionIndException;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PResToRBKMoneyPreparationResponseConverter implements Converter<ValidationResult, Message> {

    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();

        List<CardRange> cardRangeData = Optional.ofNullable(pRes.getCardRangeData())
                .map(ListWrapper::getValue)
                .orElse(emptyList())
                .stream()
                .peek(this::fillEmptyActionInd)
                .collect(toList());

        List<CardRangeDTO> addedCardRanges = new ArrayList<>();
        List<CardRangeDTO> modifiedCardRanges = new ArrayList<>();
        List<CardRangeDTO> deletedCardRanges = new ArrayList<>();

        for (CardRange cardRange : cardRangeData) {
            switch (getEnumWrapperValue(cardRange.getActionInd())) {
                case ADD_CARD_RANGE_TO_CACHE:
                    addedCardRanges.add(toDTO(cardRange));
                    break;
                case MODIFY_CARD_RANGE_DATA:
                    modifiedCardRanges.add(toDTO(cardRange));
                    break;
                case DELETE_CARD_RANGE_FROM_CACHE:
                    deletedCardRanges.add(toDTO(cardRange));
                    break;
                default:
                    throw new NullPointerActionIndException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
            }
        }

        return RBKMoneyPreparationResponse.builder()
                .providerId(providerHolder.getTag(pRes))
                .serialNum(pRes.getSerialNum())
                .addedCardRanges(addedCardRanges)
                .modifiedCardRanges(modifiedCardRanges)
                .deletedCardRanges(deletedCardRanges)
                .build();
    }

    private void fillEmptyActionInd(CardRange cardRange) {
        if (cardRange.getActionInd() == null) {
            EnumWrapper<ActionInd> addAction = new EnumWrapper<>();
            addAction.setValue(ActionInd.ADD_CARD_RANGE_TO_CACHE);

            cardRange.setActionInd(addAction);
        }
    }

    private CardRangeDTO toDTO(CardRange cardRange) {
        return CardRangeDTO.builder()
                .startRange(cardRange.getStartRange())
                .endRange(cardRange.getEndRange())
                .build();
    }
}
