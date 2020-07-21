package com.rbkmoney.threeds.server.service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesRequest;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesResponse;
import com.rbkmoney.threeds.server.converter.thrift.CardRangesConverter;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.thrift.TException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static java.lang.Long.parseLong;

public class ThreeDsServerStorageCacheService extends AbstractCacheService {

    private final ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient;
    private final ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter;
    private final Cache<String, ChallengeFlowTransactionInfo> challengeFlowTransactionInfoByTag;

    private final CardRangesStorageSrv.Iface cardRangesStorageClient;
    private final CardRangesConverter cardRangesConverter;
    private final Cache<String, Set<CardRange>> cardRangesByTag;

    public ThreeDsServerStorageCacheService(
            ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient,
            ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter,
            long challengeFlowTransactionInfoCacheSize,
            CardRangesStorageSrv.Iface cardRangesStorageClient,
            CardRangesConverter cardRangesConverter,
            long cardRangesCacheExpirationHours) {
        this.challengeFlowTransactionInfoStorageClient = challengeFlowTransactionInfoStorageClient;
        this.challengeFlowTransactionInfoConverter = challengeFlowTransactionInfoConverter;
        this.challengeFlowTransactionInfoByTag = Caffeine.newBuilder()
                .maximumSize(challengeFlowTransactionInfoCacheSize)
                .build();
        this.cardRangesStorageClient = cardRangesStorageClient;
        this.cardRangesConverter = cardRangesConverter;
        this.cardRangesByTag = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(cardRangesCacheExpirationHours))
                .build();
    }

    @Override
    Set<CardRange> getCardRanges(String tag) {
        return cardRangesByTag.get(tag, this::getCardRangesFromStorage);
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
        throw new NotImplementedException("Method 'updateCardRanges' is not supposed to be called for this CacheService implementation!");
    }

    @Override
    public boolean isInCardRange(String tag, String acctNumber) {
        return isInCardRange(tag, parseLong(acctNumber));
    }

    @Override
    @Transactional
    public void saveChallengeFlowTransactionInfo(String threeDSServerTransID, ChallengeFlowTransactionInfo challengeFlowTransactionInfo) {
        try {
            var transactionInfo = challengeFlowTransactionInfoConverter.toThrift(threeDSServerTransID, challengeFlowTransactionInfo);
            challengeFlowTransactionInfoStorageClient.saveChallengeFlowTransactionInfo(transactionInfo);
            challengeFlowTransactionInfoByTag.put(threeDSServerTransID, challengeFlowTransactionInfo);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }

    @Override
    public ChallengeFlowTransactionInfo getChallengeFlowTransactionInfo(String threeDSServerTransID) {
        return challengeFlowTransactionInfoByTag.get(threeDSServerTransID, this::getChallengeFlowTransactionInfoFromStorage);
    }

    private ChallengeFlowTransactionInfo getChallengeFlowTransactionInfoFromStorage(String threeDSServerTransID) {
        try {
            var transactionInfo = challengeFlowTransactionInfoStorageClient.getChallengeFlowTransactionInfo(threeDSServerTransID);
            return challengeFlowTransactionInfoConverter.toDomain(transactionInfo);
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
