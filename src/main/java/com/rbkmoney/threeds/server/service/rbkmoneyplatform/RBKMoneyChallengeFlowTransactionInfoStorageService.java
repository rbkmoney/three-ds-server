package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.exception.ExternalStorageException;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import org.apache.thrift.TException;

public class RBKMoneyChallengeFlowTransactionInfoStorageService implements ChallengeFlowTransactionInfoStorageService {

    private final ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient;
    private final ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter;
    private final Cache<String, ChallengeFlowTransactionInfo> transactionInfoByTransactionId;

    public RBKMoneyChallengeFlowTransactionInfoStorageService(
            ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient,
            ChallengeFlowTransactionInfoConverter challengeFlowTransactionInfoConverter,
            long challengeFlowTransactionInfoCacheSize) {
        this.challengeFlowTransactionInfoStorageClient = challengeFlowTransactionInfoStorageClient;
        this.challengeFlowTransactionInfoConverter = challengeFlowTransactionInfoConverter;
        this.transactionInfoByTransactionId = Caffeine.newBuilder()
                .maximumSize(challengeFlowTransactionInfoCacheSize)
                .build();
    }

    @Override
    public void saveChallengeFlowTransactionInfo(String threeDsServerTransId,
                                                 ChallengeFlowTransactionInfo challengeFlowTransactionInfo) {
        try {
            var transactionInfo = challengeFlowTransactionInfoConverter.toThrift(challengeFlowTransactionInfo);
            challengeFlowTransactionInfoStorageClient.saveChallengeFlowTransactionInfo(transactionInfo);
            transactionInfoByTransactionId.put(threeDsServerTransId, challengeFlowTransactionInfo);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    @Override
    public ChallengeFlowTransactionInfo getChallengeFlowTransactionInfo(String threeDsServerTransId) {
        return transactionInfoByTransactionId
                .get(threeDsServerTransId, this::getChallengeFlowTransactionInfoFromStorage);
    }

    private ChallengeFlowTransactionInfo getChallengeFlowTransactionInfoFromStorage(String threeDsServerTransId) {
        try {
            var transactionInfo =
                    challengeFlowTransactionInfoStorageClient.getChallengeFlowTransactionInfo(threeDsServerTransId);
            return challengeFlowTransactionInfoConverter.toDomain(transactionInfo);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }
}
