package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;

import java.util.List;

public interface CacheService {

    void saveSerialNum(String tag, String serialNum);

    String getSerialNum(String tag);

    void clearSerialNum(String tag);

    void updateCardRanges(String tag, List<CardRange> cardRanges);

    boolean isInCardRange(String tag, String acctNumber);

    boolean isValidCardRange(String tag, CardRange cardRange);

    void saveRReqTransactionInfo(String threeDSServerTransID, RReqTransactionInfo rReqTransactionInfo);

    RReqTransactionInfo getRReqTransactionInfo(String threeDSServerTransID);

    void clearRReqTransactionInfo(String threeDSServerTransID);

}
