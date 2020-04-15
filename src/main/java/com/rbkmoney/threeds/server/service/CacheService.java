package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;

import java.util.List;

public interface CacheService {

    void saveSerialNum(String xULTestCaseRunId, String serialNum);

    String getSerialNum(String xULTestCaseRunId);

    void clearSerialNum(String xULTestCaseRunId);

    void updateCardRanges(String xULTestCaseRunId, List<CardRange> cardRanges);

    boolean isInCardRange(String xULTestCaseRunId, String acctNumber);

    boolean isValidCardRange(String xULTestCaseRunId, CardRange cardRange);

    void saveRReqTransactionInfo(String threeDSServerTransID, RReqTransactionInfo rReqTransactionInfo);

    RReqTransactionInfo getRReqTransactionInfo(String threeDSServerTransID);

    void clearRReqTransactionInfo(String threeDSServerTransID);

}
