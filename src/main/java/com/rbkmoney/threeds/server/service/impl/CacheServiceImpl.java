package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.exeption.NullPointerActionIndException;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.rbkmoney.threeds.server.utils.CollectionsUtil.safeCollectionList;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final Map<String, String> serialNumByTag = new ConcurrentHashMap<>();
    private final Map<String, RReqTransactionInfo> rReqTransactionInfoByTag = new ConcurrentHashMap<>();
    private final Map<String, Set<CardRange>> cardRangesByTag = new ConcurrentHashMap<>();

    @Override
    public void saveSerialNum(String tag, String serialNum) {
        serialNumByTag.put(tag, serialNum);
    }

    @Override
    public String getSerialNum(String tag) {
        return serialNumByTag.get(tag);
    }

    @Override
    public void clearSerialNum(String tag) {
        serialNumByTag.remove(tag);
    }

    @Override
    public void updateCardRanges(String tag, List<CardRange> cardRanges) {
        if (!cardRangesByTag.containsKey(tag)) {
            cardRangesByTag.put(tag, new HashSet<>());
        }

        Set<CardRange> cachedCardRanges = getCardRanges(tag);

        for (CardRange cardRange : safeCollectionList(cardRanges)) {
            switch (getEnumWrapperValue(cardRange.getActionInd())) {
                case ADD_CARD_RANGE_TO_CACHE:
                    cachedCardRanges.add(cardRange);
                case MODIFY_CARD_RANGE_DATA:
                    cachedCardRanges.remove(cardRange);
                    cachedCardRanges.add(cardRange);
                    break;
                case DELETE_CARD_RANGE_FROM_CACHE:
                    cachedCardRanges.remove(cardRange);
                    break;
                default:
                    throw new NullPointerActionIndException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
            }
        }
    }

    @Override
    public boolean isInCardRange(String tag, String acctNumber) {
        if (!isTest(tag)) {
            return isInCardRange(tag, parseLong(acctNumber));
        }

        // TODO [a.romanov]: are empty ranges ok for tests?
        return getCardRanges(tag).isEmpty()
                || isInCardRange(tag, parseLong(acctNumber));
    }

    @Override
    public boolean isValidCardRange(String tag, CardRange cardRange) {
        Set<CardRange> cachedCardRanges = getCardRanges(tag);
        long startRange = parseLong(cardRange.getStartRange());
        long endRange = parseLong(cardRange.getEndRange());
        ActionInd actionInd = getEnumWrapperValue(cardRange.getActionInd());

        if (actionInd == null) {
            return true;
        }

        switch (actionInd) {
            case ADD_CARD_RANGE_TO_CACHE:
                return cachedCardRanges.isEmpty() || isValidForAddCardRange(tag, startRange, endRange);
            case MODIFY_CARD_RANGE_DATA:
            case DELETE_CARD_RANGE_FROM_CACHE:
                return cachedCardRanges.isEmpty() || isValidForModifyOrDeleteCardRange(tag, startRange, endRange);
            default:
                throw new NullPointerActionIndException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
        }
    }

    @Override
    public void saveRReqTransactionInfo(String threeDSServerTransID, RReqTransactionInfo rReqTransactionInfo) {
        rReqTransactionInfoByTag.put(threeDSServerTransID, rReqTransactionInfo);
    }

    @Override
    public RReqTransactionInfo getRReqTransactionInfo(String threeDSServerTransID) {
        return rReqTransactionInfoByTag.get(threeDSServerTransID);
    }

    @Override
    public void clearRReqTransactionInfo(String threeDSServerTransID) {
        rReqTransactionInfoByTag.remove(threeDSServerTransID);
    }

    private boolean isInCardRange(String tag, Long acctNumber) {
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

    private Set<CardRange> getCardRanges(String tag) {
        return cardRangesByTag.getOrDefault(tag, new HashSet<>());
    }

    private boolean isTest(String tag) {
        return stream(DirectoryServerProvider.values())
                .filter(provider -> provider == DirectoryServerProvider.TEST)
                .noneMatch(provider -> provider.getTag().equals(tag));
    }
}
