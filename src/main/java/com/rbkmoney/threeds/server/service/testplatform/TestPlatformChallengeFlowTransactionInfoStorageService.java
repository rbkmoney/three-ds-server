package com.rbkmoney.threeds.server.service.testplatform;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;

public class TestPlatformChallengeFlowTransactionInfoStorageService
        implements ChallengeFlowTransactionInfoStorageService {

    private final Cache<String, ChallengeFlowTransactionInfo> transactionInfoById;

    public TestPlatformChallengeFlowTransactionInfoStorageService() {
        this.transactionInfoById = Caffeine.newBuilder()
                .maximumSize(1000)
                .build();
    }

    @Override
    public void saveChallengeFlowTransactionInfo(String threeDsServerTransId,
                                                 ChallengeFlowTransactionInfo challengeFlowTransactionInfo) {
        transactionInfoById.put(threeDsServerTransId, challengeFlowTransactionInfo);
    }

    @Override
    public ChallengeFlowTransactionInfo getChallengeFlowTransactionInfo(String threeDsServerTransId) {
        return transactionInfoById.getIfPresent(threeDsServerTransId);
    }
}
