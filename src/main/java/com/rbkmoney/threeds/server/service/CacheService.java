package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;

import java.util.List;

public interface CacheService {

    void saveSerialNum(String tag, String serialNum);

    String getSerialNum(String tag);

    void clearSerialNum(String tag);

    void updateCardRanges(String tag, List<CardRange> cardRanges);

    boolean isInCardRange(String tag, String acctNumber);

    boolean isValidCardRange(String tag, CardRange cardRange);

    void saveChallengeFlowTransactionInfo(String threeDSServerTransID, ChallengeFlowTransactionInfo challengeFlowTransactionInfo);

    ChallengeFlowTransactionInfo getChallengeFlowTransactionInfo(String threeDSServerTransID);

}
