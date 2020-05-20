package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.threeds.server.domain.CardRange;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CardRangesConverter {

    public Set<CardRange> toDomain(
            List<com.rbkmoney.damsel.three_ds_server_storage.CardRange> thriftCardRanges) {
        return thriftCardRanges.stream()
                .map(this::toDomain)
                .collect(toSet());
    }

    private CardRange toDomain(
            com.rbkmoney.damsel.three_ds_server_storage.CardRange thriftCardRange) {
        CardRange domain = new CardRange();
        domain.setStartRange(String.valueOf(thriftCardRange.getRangeStart()));
        domain.setEndRange(String.valueOf(thriftCardRange.getRangeEnd()));

        return domain;
    }
}
