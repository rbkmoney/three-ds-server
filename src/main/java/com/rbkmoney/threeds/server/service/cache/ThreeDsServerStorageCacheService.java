package com.rbkmoney.threeds.server.service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesRequest;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesResponse;
import com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.CardRangesConverter;
import com.rbkmoney.threeds.server.converter.RReqTransactionInfoConverter;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.thrift.TException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static java.lang.Long.parseLong;

public class ThreeDsServerStorageCacheService extends AbstractCacheService {

    private final RReqTransactionInfoStorageSrv.Iface rReqTransactionInfoStorageClient;
    private final RReqTransactionInfoConverter rReqTransactionInfoConverter;
    private final Cache<String, RReqTransactionInfo> rReqTransactionInfoByTag;

    private final CardRangesStorageSrv.Iface cardRangesStorageClient;
    private final CardRangesConverter cardRangesConverter;
    private final Cache<String, Set<CardRange>> cardRangesByTag;

    public ThreeDsServerStorageCacheService(
            RReqTransactionInfoStorageSrv.Iface rReqTransactionInfoStorageClient,
            RReqTransactionInfoConverter rReqTransactionInfoConverter,
            long rReqTransactionInfoCacheSize,
            CardRangesStorageSrv.Iface cardRangesStorageClient,
            CardRangesConverter cardRangesConverter,
            long cardRangesCacheExpirationHours) {
        this.rReqTransactionInfoStorageClient = rReqTransactionInfoStorageClient;
        this.rReqTransactionInfoConverter = rReqTransactionInfoConverter;
        this.rReqTransactionInfoByTag = Caffeine.newBuilder()
                .maximumSize(rReqTransactionInfoCacheSize)
                .build();
        this.cardRangesStorageClient = cardRangesStorageClient;
        this.cardRangesConverter = cardRangesConverter;
        this.cardRangesByTag = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(cardRangesCacheExpirationHours))
                .build();
    }

    @Override
    Set<CardRange> getCardRanges(String tag) {
        return cardRangesByTag.get(
                tag,
                this::getCardRangesFromStorage);
    }

    @Override
    public void saveSerialNum(String tag, String serialNum) {
        throw new NotImplementedException("Method 'saveSerialNum' is not supposed to be called for this CacheService implementation!");
    }

    @Override
    public String getSerialNum(String tag) {
        throw new NotImplementedException("Method 'getSerialNum' is not supposed to be called for this CacheService implementation!");
    }

    @Override
    public void clearSerialNum(String tag) {
        throw new NotImplementedException("Method 'clearSerialNum' is not supposed to be called for this CacheService implementation!");
    }

    @Override
    public void updateCardRanges(String tag, List<CardRange> cardRanges) {
        // TODO [a.romanov]: save to storage
    }

    @Override
    public boolean isInCardRange(String tag, String acctNumber) {
        return isInCardRange(tag, parseLong(acctNumber));
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

    private RReqTransactionInfo getRReqTransactionInfoFromStorage(String threeDSServerTransID) {
        try {
            var transactionInfo = rReqTransactionInfoStorageClient.getRReqTransactionInfo(threeDSServerTransID);
            return rReqTransactionInfoConverter.toDomain(transactionInfo);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }

    private Set<CardRange> getCardRangesFromStorage(String tag) {
        try {
            GetCardRangesRequest request = new GetCardRangesRequest(tag);
            GetCardRangesResponse response = cardRangesStorageClient.getCardRanges(request);
            return cardRangesConverter.toDomain(response.getCardRanges());
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }
}
