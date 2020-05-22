package com.rbkmoney.threeds.server.service.cache;

import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.exeption.NullPointerActionIndException;

import java.util.Set;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.lang.Long.parseLong;

public abstract class AbstractCacheService implements CacheService {

    abstract Set<CardRange> getCardRanges(String tag);

    @Override
    public boolean isValidCardRange(String tag, CardRange cardRange) {
        ActionInd actionInd = getEnumWrapperValue(cardRange.getActionInd());
        if (actionInd == null) {
            return true;
        }

        Set<CardRange> cachedCardRanges = getCardRanges(tag);
        long startRange = parseLong(cardRange.getStartRange());
        long endRange = parseLong(cardRange.getEndRange());

        switch (actionInd) {
            case ADD_CARD_RANGE_TO_CACHE:
                return cachedCardRanges.isEmpty() || isValidForAddCardRange(tag, startRange, endRange);
            case MODIFY_CARD_RANGE_DATA:
            case DELETE_CARD_RANGE_FROM_CACHE:
                return cachedCardRanges.isEmpty() || isValidForModifyOrDeleteCardRange(tag, startRange, endRange);
            default:
                throw new NullPointerActionIndException("Missing Action Indicator in Card Range Data, cardRange=" + cardRange);
        }
    }

    boolean isInCardRange(String tag, Long acctNumber) {
        Set<CardRange> cachedCardRanges = getCardRanges(tag);
        return cachedCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) <= acctNumber
                                && acctNumber <= parseLong(cardRange.getEndRange()));
    }

    private boolean isValidForAddCardRange(String tag, long startRange, long endRange) {
        Set<CardRange> cachedCardRanges = getCardRanges(tag);
        return cachedCardRanges.stream()
                .allMatch(
                        cardRange -> isFromTheLeftSide(startRange, endRange, cardRange)
                                || isFromTheRightSide(startRange, endRange, cardRange));
    }

    private boolean isValidForModifyOrDeleteCardRange(String tag, long startRange, long endRange) {
        Set<CardRange> cachedCardRanges = getCardRanges(tag);
        return cachedCardRanges.stream()
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
