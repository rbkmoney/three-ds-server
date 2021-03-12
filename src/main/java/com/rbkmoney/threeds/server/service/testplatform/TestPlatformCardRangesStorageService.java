package com.rbkmoney.threeds.server.service.testplatform;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.ADD_CARD_RANGE_TO_CACHE;
import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.DELETE_CARD_RANGE_FROM_CACHE;
import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.MODIFY_CARD_RANGE_DATA;
import static com.rbkmoney.threeds.server.utils.Collections.safeList;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static java.lang.Long.parseLong;

public class TestPlatformCardRangesStorageService {

    private final Cache<String, Set<CardRange>> cardRangesById;

    public TestPlatformCardRangesStorageService() {
        this.cardRangesById = Caffeine.newBuilder()
                .maximumSize(1000)
                .build();
    }

    public void updateCardRanges(PRes pRes) {
        String ulTestCaseId = pRes.getUlTestCaseId();
        List<CardRange> cardRanges = cardRanges(pRes);
        if (!cardRanges.isEmpty()) {
            boolean isNeedStorageClear = isNeedStorageClear(pRes);

            Set<CardRange> storageCardRanges = getStorageCardRanges(ulTestCaseId);

            if (isNeedStorageClear) {
                storageCardRanges.clear();
            } else {
                cardRanges.stream()
                        .filter(cardRange -> getValue(cardRange.getActionInd()) == DELETE_CARD_RANGE_FROM_CACHE)
                        .forEach(storageCardRanges::remove);
            }

            cardRanges.stream()
                    .filter(cardRange -> getValue(cardRange.getActionInd()) == ADD_CARD_RANGE_TO_CACHE
                            || getValue(cardRange.getActionInd()) == MODIFY_CARD_RANGE_DATA)
                    .forEach(storageCardRanges::add);
        }
    }

    public boolean isValidCardRanges(PRes pRes) {
        String ulTestCaseId = pRes.getUlTestCaseId();
        List<CardRange> cardRanges = cardRanges(pRes);
        boolean isNeedStorageClear = isNeedStorageClear(pRes);

        Set<CardRange> storageCardRanges = getStorageCardRanges(ulTestCaseId);

        if (!cardRanges.isEmpty() && !isNeedStorageClear && !storageCardRanges.isEmpty()) {
            return cardRanges.stream()
                    .allMatch(cardRange -> isValidCardRange(storageCardRanges, cardRange));
        } else {
            return true;
        }
    }

    public boolean isInCardRange(String ulTestCaseId, String acctNumber) {
        Set<CardRange> storageCardRanges = getStorageCardRanges(ulTestCaseId);

        if (storageCardRanges.isEmpty()) {
            return true;
        }

        return isInCardRange(storageCardRanges, parseLong(acctNumber));
    }

    private boolean isInCardRange(Set<CardRange> storageCardRanges, Long acctNumber) {
        return storageCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) <= acctNumber
                                && acctNumber <= parseLong(cardRange.getEndRange()));
    }

    private Set<CardRange> getStorageCardRanges(String ulTestCaseId) {
        Set<CardRange> cardRanges = cardRangesById.getIfPresent(ulTestCaseId);
        if (cardRanges == null) {
            cardRanges = new HashSet<>();
            cardRangesById.put(ulTestCaseId, cardRanges);
        }
        return cardRanges;
    }

    private boolean isValidCardRange(Set<CardRange> storageCardRanges, CardRange cardRange) {
        long startRange = parseLong(cardRange.getStartRange());
        long endRange = parseLong(cardRange.getEndRange());
        ActionInd actionInd = getValue(cardRange.getActionInd());

        switch (actionInd) {
            case ADD_CARD_RANGE_TO_CACHE:
                if (existsCardRange(storageCardRanges, startRange, endRange)) {
                    return true;
                }

                return existsFreeSpaceForNewCardRange(storageCardRanges, startRange, endRange);
            case MODIFY_CARD_RANGE_DATA:
            case DELETE_CARD_RANGE_FROM_CACHE:
                return existsCardRange(storageCardRanges, startRange, endRange);
            default:
                throw new IllegalArgumentException(
                        String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
        }
    }

    private boolean existsFreeSpaceForNewCardRange(Set<CardRange> storageCardRanges, long startRange, long endRange) {
        return storageCardRanges.stream()
                .allMatch(
                        cardRange -> endRange < parseLong(cardRange.getStartRange())
                                || parseLong(cardRange.getEndRange()) < startRange);
    }

    private boolean existsCardRange(Set<CardRange> storageCardRanges, long startRange, long endRange) {
        return storageCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) == startRange
                                && parseLong(cardRange.getEndRange()) == endRange);
    }

    private List<CardRange> cardRanges(PRes pRes) {
        return safeList(pRes.getCardRangeData()).stream()
                .peek(this::fillEmptyActionInd)
                .collect(Collectors.toList());
    }

    private boolean isNeedStorageClear(PRes pRes) {
        return Optional.ofNullable((pRes.getRequestMessage()))
                .map(message -> (PReq) message)
                .map(PReq::getSerialNum)
                .isEmpty();
    }

    private void fillEmptyActionInd(CardRange cardRange) {
        if (cardRange.getActionInd() == null) {
            EnumWrapper<ActionInd> addAction = new EnumWrapper<>();
            addAction.setValue(ADD_CARD_RANGE_TO_CACHE);

            cardRange.setActionInd(addAction);
        }
    }
}
