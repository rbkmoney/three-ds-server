package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.cardrange.CardRange;

import java.util.List;

public interface CardRangesStorageService {

    boolean isValidCardRanges(String tag, List<CardRange> cardRanges);

    boolean isInCardRange(String tag, String acctNumber);

}
