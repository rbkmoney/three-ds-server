package com.rbkmoney.threeds.server.service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.exeption.NullPointerActionIndException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.rbkmoney.threeds.server.utils.CollectionsUtil.safeCollectionList;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.lang.Long.parseLong;

public class InMemoryCacheService extends AbstractCacheService {

    private final Map<String, String> serialNumByTag = new ConcurrentHashMap<>();
    private final Map<String, Set<CardRange>> cardRangesByTag = new ConcurrentHashMap<>();
    private final Cache<String, RReqTransactionInfo> rReqTransactionInfoByTag = Caffeine.newBuilder()
            .maximumSize(1000L)
            .build();

    @Override
    Set<CardRange> getCardRanges(String tag) {
        return cardRangesByTag.getOrDefault(tag, new HashSet<>());
    }

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
        return getCardRanges(tag).isEmpty()
                || isInCardRange(tag, parseLong(acctNumber));
    }

    @Override
    public void saveRReqTransactionInfo(String threeDSServerTransID, RReqTransactionInfo rReqTransactionInfo) {
        rReqTransactionInfoByTag.put(threeDSServerTransID, rReqTransactionInfo);
    }

    @Override
    public RReqTransactionInfo getRReqTransactionInfo(String threeDSServerTransID) {
        return rReqTransactionInfoByTag.getIfPresent(threeDSServerTransID);
    }
}
