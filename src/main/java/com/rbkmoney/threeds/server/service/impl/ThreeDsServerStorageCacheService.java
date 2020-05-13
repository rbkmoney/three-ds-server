package com.rbkmoney.threeds.server.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesRequest;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesResponse;
import com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.converter.RReqTransactionInfoConverter;
import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.exeption.NullPointerActionIndException;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.thrift.TException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;

//@Service
@RequiredArgsConstructor
public class ThreeDsServerStorageCacheService implements CacheService {

    private final Cache<String, Set<CardRange>> cardRangesByTag = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1L)) // TODO [a.romanov]: duration
            .build();

    private final Cache<String, RReqTransactionInfo> rReqTransactionInfoByTag = Caffeine.newBuilder()
            .maximumSize(1000L) // TODO [a.romanov]: size?
            .build();

    private final RReqTransactionInfoStorageSrv.Iface rReqTransactionInfoStorageClient;
    private final RReqTransactionInfoConverter rReqTransactionInfoConverter;
    private final CardRangesStorageSrv.Iface cardRangesStorageClient;

    @Override
    public void saveSerialNum(String tag, String serialNum) {
        throw new NotImplementedException("TODO [a.romanov]");
    }

    @Override
    public String getSerialNum(String tag) {
        throw new NotImplementedException("TODO [a.romanov]");
    }

    @Override
    public void clearSerialNum(String tag) {
        throw new NotImplementedException("TODO [a.romanov]");
    }

    @Override
    public void updateCardRanges(String tag, List<CardRange> cardRanges) {
        // TODO [a.romanov]: save to storage and save to cache
    }

    @Override
    public boolean isInCardRange(String tag, String acctNumber) {
        // TODO [a.romanov]: remove after BJ-893
        if (!isTest(tag)) {
            return isInCardRange(tag, parseLong(acctNumber));
        }

        return getCardRanges(tag).isEmpty()
                || isInCardRange(tag, parseLong(acctNumber));
    }

    @Override
    public boolean isValidCardRange(String tag, CardRange cardRange) {
        ActionInd actionInd = getEnumWrapperValue(cardRange.getActionInd());
        // TODO [a.romanov]: test case only?
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
                throw new NullPointerActionIndException(String.format("Action Indicator missing in Card Range Data, cardRange=%s", cardRange));
        }
    }

    @Override
    @Transactional
    public void saveRReqTransactionInfo(String threeDSServerTransID, RReqTransactionInfo rReqTransactionInfo) {
        try {
            var transactionInfo = rReqTransactionInfoConverter.toThrift(threeDSServerTransID, rReqTransactionInfo);
            rReqTransactionInfoStorageClient.saveRReqTransactionInfo(transactionInfo);
            rReqTransactionInfoByTag.put(threeDSServerTransID, rReqTransactionInfo);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }

    @Override
    public RReqTransactionInfo getRReqTransactionInfo(String threeDSServerTransID) {
        return rReqTransactionInfoByTag.get(
                threeDSServerTransID,
                this::getRReqTransactionInfoFromStorage);
    }

    private Set<CardRange> getCardRanges(String tag) {
        return cardRangesByTag.get(
                tag,
                this::getCardRangesFromStorage);
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

    private boolean isInCardRange(String tag, Long acctNumber) {
        Set<CardRange> cachedCardRanges = getCardRanges(tag);
        return cachedCardRanges.stream()
                .anyMatch(
                        cardRange -> parseLong(cardRange.getStartRange()) <= acctNumber
                                && acctNumber <= parseLong(cardRange.getEndRange()));
    }

    private boolean isTest(String tag) {
        return stream(DirectoryServerProvider.values())
                .filter(provider -> provider != DirectoryServerProvider.TEST)
                .noneMatch(provider -> provider.getTag().equals(tag));
    }

    private RReqTransactionInfo getRReqTransactionInfoFromStorage(String threeDSServerTransID) {
        try {
            var transactionInfo = rReqTransactionInfoStorageClient.getRReqTransactionInfo(threeDSServerTransID);
            return rReqTransactionInfoConverter.toDTO(transactionInfo);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }

    private Set<CardRange> getCardRangesFromStorage(String tag) {
        try {
            GetCardRangesResponse response = cardRangesStorageClient.getCardRanges(new GetCardRangesRequest(tag));
            // TODO [a.romanov]: convert
            return null;
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }
}
