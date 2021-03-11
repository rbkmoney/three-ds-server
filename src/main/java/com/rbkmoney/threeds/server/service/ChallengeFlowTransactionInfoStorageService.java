package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;

public interface ChallengeFlowTransactionInfoStorageService {

    void saveChallengeFlowTransactionInfo(String threeDSServerTransID,
                                          ChallengeFlowTransactionInfo challengeFlowTransactionInfo);

    ChallengeFlowTransactionInfo getChallengeFlowTransactionInfo(String threeDSServerTransID);

}
