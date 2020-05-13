package com.rbkmoney.threeds.server.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.damsel.three_ds_server_storage.RReqTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.RReqTransactionInfoConverter;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.exeption.ThreeDsServerStorageException;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.thrift.TException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Service
@RequiredArgsConstructor
public class ThreeDsServerStorageCacheService implements CacheService {

    private final Cache<String, RReqTransactionInfo> rReqTransactionInfoByTag = Caffeine.newBuilder()
            .maximumSize(1000L) // TODO [a.romanov]: size?
            .build();

    private final RReqTransactionInfoStorageSrv.Iface rReqTransactionInfoStorageClient;
    private final RReqTransactionInfoConverter rReqTransactionInfoConverter;

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
        // TODO [a.romanov]: check using cache
        return false;
    }

    @Override
    public boolean isValidCardRange(String tag, CardRange cardRange) {
        // TODO [a.romanov]: check using cache
        return false;
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

    private RReqTransactionInfo getRReqTransactionInfoFromStorage(String id) {
        try {
            var transactionInfo = rReqTransactionInfoStorageClient.getRReqTransactionInfo(id);
            return rReqTransactionInfoConverter.toDTO(transactionInfo);
        } catch (TException e) {
            throw new ThreeDsServerStorageException(e);
        }
    }
}