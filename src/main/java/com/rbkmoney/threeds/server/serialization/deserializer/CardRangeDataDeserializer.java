package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.serialization.AbstractListDeserializer;

public class CardRangeDataDeserializer extends AbstractListDeserializer<CardRange> {

    @Override
    protected Class<CardRange> getClassType() {
        return CardRange.class;
    }
}
