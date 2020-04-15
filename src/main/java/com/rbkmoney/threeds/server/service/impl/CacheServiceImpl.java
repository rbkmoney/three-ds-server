package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.exeption.NullPointerActionIndException;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.rbkmoney.threeds.server.utils.CollectionsUtil.safeCollectionList;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final Map<String, String> serialNumById = new ConcurrentHashMap<>();
    private final Map<String, RReqTransactionInfo> rReqTransactionInfoById = new ConcurrentHashMap<>();
    private final Map<String, Set<CardRange>> cashedCardRangesMap = new ConcurrentHashMap<>();

    @Override
    public void saveSerialNum(String xULTestCaseRunId, String serialNum) {
        serialNumById.put(xULTestCaseRunId, serialNum);
    }

    @Override
    public String getSerialNum(String xULTestCaseRunId) {
        return serialNumById.get(xULTestCaseRunId);
    }

    @Override
    public void clearSerialNum(String xULTestCaseRunId) {
        serialNumById.remove(xULTestCaseRunId);
    }

    @Override
    public void updateCardRanges(String xULTestCaseRunId, List<CardRange> cardRanges) {
        if (!cashedCardRangesMap.containsKey(xULTestCaseRunId)) {
            cashedCardRangesMap.put(xULTestCaseRunId, new HashSet<>());
        }
        Set<CardRange> cashedCardRanges = getCashedCardRanges(xULTestCaseRunId);
        safeCollectionList(cardRanges).forEach(
                cardRange -> {
                    switch (getEnumWrapperValue(cardRange.getActionInd())) {
                        case ADD_CARD_RANGE_TO_CACHE:
                            cashedCardRanges.add(cardRange);
                        case MODIFY_CARD_RANGE_DATA:
                            cashedCardRanges.remove(cardRange);
                            cashedCardRanges.add(cardRange);
                            break;
                        case DELETE_CARD_RANGE_FROM_CACHE:
                            cashedCardRanges.remove(cardRange);
                            break;
                        default:
                            throw new NullPointerActionIndException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
                    }
                }
        );
    }

    @Override
    public boolean isInCardRange(String xULTestCaseRunId, String acctNumber) {
        return getCashedCardRanges(xULTestCaseRunId).isEmpty()
                || isInCardRange(xULTestCaseRunId, parseLong(acctNumber));
    }

    @Override
    public boolean isValidCardRange(String xULTestCaseRunId, CardRange cardRange) {
        Set<CardRange> cashedCardRanges = getCashedCardRanges(xULTestCaseRunId);
        long startRange = parseLong(cardRange.getStartRange());
        long endRange = parseLong(cardRange.getEndRange());
        ActionInd actionInd = getEnumWrapperValue(cardRange.getActionInd());

        if (actionInd == null) {
            return true;
        }

        switch (actionInd) {
            case ADD_CARD_RANGE_TO_CACHE:
                return cashedCardRanges.isEmpty() || isValidForAddCardRange(xULTestCaseRunId, startRange, endRange);
            case MODIFY_CARD_RANGE_DATA:
            case DELETE_CARD_RANGE_FROM_CACHE:
                return cashedCardRanges.isEmpty() || isValidForModifyOrDeleteCardRange(xULTestCaseRunId, startRange, endRange);
            default:
                throw new NullPointerActionIndException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
        }
    }

    @Override
    public void saveRReqTransactionInfo(String threeDSServerTransID, RReqTransactionInfo rReqTransactionInfo) {
        rReqTransactionInfoById.put(threeDSServerTransID, rReqTransactionInfo);
    }

    @Override
    public RReqTransactionInfo getRReqTransactionInfo(String threeDSServerTransID) {
        return rReqTransactionInfoById.get(threeDSServerTransID);
    }

    @Override
    public void clearRReqTransactionInfo(String threeDSServerTransID) {
        rReqTransactionInfoById.remove(threeDSServerTransID);
    }

    private boolean isInCardRange(String xULTestCaseRunId, Long acctNumber) {
        Set<CardRange> cashedCardRanges = getCashedCardRanges(xULTestCaseRunId);
        return cashedCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) <= acctNumber
                                && acctNumber <= parseLong(cardRange.getEndRange())
                );
    }

    private boolean isValidForAddCardRange(String xULTestCaseRunId, long startRange, long endRange) {
        Set<CardRange> cashedCardRanges = getCashedCardRanges(xULTestCaseRunId);
        return cashedCardRanges.stream()
                .allMatch(
                        cr -> isFromTheLeftSide(startRange, endRange, cr)
                                || isFromTheRightSide(startRange, endRange, cr)
                );
    }

    private boolean isValidForModifyOrDeleteCardRange(String xULTestCaseRunId, long startRange, long endRange) {
        Set<CardRange> cashedCardRanges = getCashedCardRanges(xULTestCaseRunId);
        return cashedCardRanges.stream()
                .anyMatch(
                        cr -> parseLong(cr.getStartRange()) == startRange
                                && parseLong(cr.getEndRange()) == endRange
                );
    }

    private boolean isFromTheLeftSide(long startRange, long endRange, CardRange cr) {
        return startRange < endRange && endRange < parseLong(cr.getStartRange());
    }

    private boolean isFromTheRightSide(long startRange, long endRange, CardRange cr) {
        return parseLong(cr.getEndRange()) < startRange && startRange < endRange;
    }

    private Set<CardRange> getCashedCardRanges(String xULTestCaseRunId) {
        return cashedCardRangesMap.getOrDefault(xULTestCaseRunId, new HashSet<>());
    }
}
