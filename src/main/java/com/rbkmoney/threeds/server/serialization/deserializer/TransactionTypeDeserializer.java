package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.transaction.TransactionType;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class TransactionTypeDeserializer extends AbstractEnumDeserializer<TransactionType> {

    @Override
    protected TransactionType enumValueOf(String candidate) {
        return TransactionType.valueOf(candidate);
    }

    @Override
    protected TransactionType[] enumValues() {
        return TransactionType.values();
    }
}
