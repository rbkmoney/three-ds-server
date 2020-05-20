package com.rbkmoney.threeds.server.handle.constraint.pres.cardrangedata;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintType;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.pres.PResConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.CollectionsUtil.safeCollectionList;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
public class CardRangeContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    private final DirectoryServerProviderHolder providerHolder;
    private final CacheService cacheService;
    private final Validator validator;

    @Override
    public boolean canHandle(PRes o) {
        return o.getCardRangeData() != null;
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        if (o.getCardRangeData().isGarbage()) {
            o.setHandleRepetitionNeeded(true);
            return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
        }

        List<CardRange> cardRangeData = o.getCardRangeData().getValue();
        if (cardRangeData.isEmpty()) {
            o.setHandleRepetitionNeeded(true);
            return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
        }

        for (CardRange cardRange : safeCollectionList(cardRangeData)) {
            Set<ConstraintViolation<CardRange>> cardRangeErrors = validator.validate(cardRange);
            if (!cardRangeErrors.isEmpty()) {
                o.setHandleRepetitionNeeded(true);

                ConstraintViolation<CardRange> cardRangeError = cardRangeErrors.iterator().next();
                ConstraintType constraintType = getConstraintType(cardRangeError);
                String fieldName = cardRangeError.getPropertyPath().toString();
                return ConstraintValidationResult.failure(constraintType, fieldName);
            }
        }

        List<CardRange> newCardRanges = filterNewCardRanges(cardRangeData);

        List<Map.Entry<Long, Long>> validNewCardRanges = filterInvalidCardRanges(newCardRanges);

        if (validNewCardRanges.size() != newCardRanges.size()) {
            o.setHandleRepetitionNeeded(true);

            return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
        }

        for (CardRange cardRange : safeCollectionList(cardRangeData)) {
            if (!cacheService.isValidCardRange(providerHolder.getTag(o), cardRange)) {
                o.setHandleRepetitionNeeded(true);

                return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
            }
        }

        return ConstraintValidationResult.success();
    }

    private List<CardRange> filterNewCardRanges(List<CardRange> cardRangeData) {
        return safeCollectionList(cardRangeData).stream()
                .filter(cardRange -> getEnumWrapperValue(cardRange.getActionInd()) == null)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<Long, Long>> filterInvalidCardRanges(List<CardRange> newCardRanges) {
        List<Map.Entry<Long, Long>> validCardRanges = new ArrayList<>();

        List<Map.Entry<Long, Long>> cardRanges = newCardRanges.stream()
                .map(cr -> new AbstractMap.SimpleEntry<>(parseLong(cr.getStartRange()), parseLong(cr.getEndRange())))
                .filter(entry -> entry.getKey() < entry.getValue())
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        for (int i = 1; i < cardRanges.size(); i++) {
            Map.Entry<Long, Long> prevCardRange = cardRanges.get(i - 1);
            Map.Entry<Long, Long> cardRange = cardRanges.get(i);
            if (prevCardRange.getValue() < cardRange.getKey()) {
                validCardRanges.add(prevCardRange);
                if (i == cardRanges.size() - 1) {
                    validCardRanges.add(cardRange);
                }
            }
        }
        return validCardRanges;
    }

    private ConstraintType getConstraintType(ConstraintViolation<CardRange> cardRangeError) {
        return ConstraintType.of(cardRangeError.getMessageTemplate());
    }
}
