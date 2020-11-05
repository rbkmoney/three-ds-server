package com.rbkmoney.threeds.server.service.testplatform;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static java.lang.Long.parseLong;

public class TestPlatformCardRangesStorageService implements CardRangesStorageService {

    private final Cache<String, Set<CardRange>> cardRangesById;

    public TestPlatformCardRangesStorageService() {
        this.cardRangesById = Caffeine.newBuilder()
                .maximumSize(1000)
                .build();
    }

    public void updateCardRanges(String ulTestCaseId, List<CardRange> newCardRanges) {
        Set<CardRange> storageCardRanges = getStorageCardRanges(ulTestCaseId);

        for (CardRange newCardRange : newCardRanges) {
            switch (getValue(newCardRange.getActionInd())) {
                case ADD_CARD_RANGE_TO_CACHE:
                    storageCardRanges.add(newCardRange);
                    break;
                case MODIFY_CARD_RANGE_DATA:
                    storageCardRanges.remove(newCardRange);
                    storageCardRanges.add(newCardRange);
                    break;
                case DELETE_CARD_RANGE_FROM_CACHE:
                    storageCardRanges.remove(newCardRange);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", newCardRange));
            }
        }
    }

    @Override
    public boolean isValidCardRanges(String ulTestCaseId, List<CardRange> cardRanges) {
        Set<CardRange> storageCardRanges = getStorageCardRanges(ulTestCaseId);

        if (storageCardRanges.isEmpty()) {
            return true;
        }

        return cardRanges.stream()
                .filter(cardRange -> getValue(cardRange.getActionInd()) != null)
                .allMatch(cardRange -> isValidCardRange(storageCardRanges, cardRange));
    }

    @Override
    public boolean isInCardRange(String ulTestCaseId, String acctNumber) {
        Set<CardRange> storageCardRanges = getStorageCardRanges(ulTestCaseId);

        if (storageCardRanges.isEmpty()) {
            return true;
        }

        return anyMatchAcctNumber(storageCardRanges, parseLong(acctNumber));
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
                return isValidForAddCardRange(storageCardRanges, startRange, endRange);
            case MODIFY_CARD_RANGE_DATA:
            case DELETE_CARD_RANGE_FROM_CACHE:
                return isValidForModifyOrDeleteCardRange(storageCardRanges, startRange, endRange);
            default:
                throw new IllegalArgumentException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
        }
    }

    private boolean anyMatchAcctNumber(Set<CardRange> storageCardRanges, Long acctNumber) {
        return storageCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) <= acctNumber
                                && acctNumber <= parseLong(cardRange.getEndRange()));
    }

    private boolean isValidForAddCardRange(Set<CardRange> storageCardRanges, long startRange, long endRange) {
        return storageCardRanges.stream()
                .allMatch(
                        cardRange -> isFromTheLeftSide(startRange, endRange, cardRange)
                                || isFromTheRightSide(startRange, endRange, cardRange));
    }

    private boolean isValidForModifyOrDeleteCardRange(Set<CardRange> storageCardRanges, long startRange, long endRange) {
        return storageCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) == startRange
                                && parseLong(cardRange.getEndRange()) == endRange);
    }

    private boolean isFromTheLeftSide(long startRange, long endRange, CardRange cardRange) {
        return startRange < endRange && endRange < parseLong(cardRange.getStartRange());
    }

    private boolean isFromTheRightSide(long startRange, long endRange, CardRange cardRange) {
        return parseLong(cardRange.getEndRange()) < startRange && startRange < endRange;
    }
}
