package com.rbkmoney.threeds.server.converter;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CardRangesConverter {

    public Set<com.rbkmoney.threeds.server.domain.CardRange> toDomain(
            List<com.rbkmoney.damsel.three_ds_server_storage.CardRange> cardRanges) {
        return cardRanges.stream()
                .map(this::toDomain)
                .collect(toSet());
    }

    private com.rbkmoney.threeds.server.domain.CardRange toDomain(
            com.rbkmoney.damsel.three_ds_server_storage.CardRange cardRange) {
        var domain = new com.rbkmoney.threeds.server.domain.CardRange();
        domain.setStartRange(String.valueOf(cardRange.getRangeStart()));
        domain.setEndRange(String.valueOf(cardRange.getRangeEnd()));

        return domain;
    }
}
