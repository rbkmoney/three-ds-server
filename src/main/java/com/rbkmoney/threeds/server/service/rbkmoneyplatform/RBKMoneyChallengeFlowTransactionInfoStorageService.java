package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.exeption.ExternalStorageException;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import org.apache.thrift.TException;

public class RBKMoneyChallengeFlowTransactionInfoStorageService implements ChallengeFlowTransactionInfoStorageService {

    private final ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient;
    private final ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter;
    private final Cache<String, ChallengeFlowTransactionInfo> transactionInfoByTransactionId;

    public RBKMoneyChallengeFlowTransactionInfoStorageService(ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient,
                                                              ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter,
                                                              long challengeFlowTransactionInfoCacheSize) {
        this.challengeFlowTransactionInfoStorageClient = challengeFlowTransactionInfoStorageClient;
        this.challengeFlowTransactionInfoConverter = challengeFlowTransactionInfoConverter;
        this.transactionInfoByTransactionId = Caffeine.newBuilder()
                .maximumSize(challengeFlowTransactionInfoCacheSize)
                .build();
    }

    @Override
    public void saveChallengeFlowTransactionInfo(String threeDSServerTransID, ChallengeFlowTransactionInfo challengeFlowTransactionInfo) {
        try {
            var transactionInfo = challengeFlowTransactionInfoConverter.toThrift(challengeFlowTransactionInfo);
            challengeFlowTransactionInfoStorageClient.saveChallengeFlowTransactionInfo(transactionInfo);
            transactionInfoByTransactionId.put(threeDSServerTransID, challengeFlowTransactionInfo);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    @Override
    public ChallengeFlowTransactionInfo getChallengeFlowTransactionInfo(String threeDSServerTransID) {
        return transactionInfoByTransactionId.get(threeDSServerTransID, this::getChallengeFlowTransactionInfoFromStorage);
    }

    private ChallengeFlowTransactionInfo getChallengeFlowTransactionInfoFromStorage(String threeDSServerTransID) {
        try {
            var transactionInfo = challengeFlowTransactionInfoStorageClient.getChallengeFlowTransactionInfo(threeDSServerTransID);
            return challengeFlowTransactionInfoConverter.toDomain(transactionInfo);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }
}
