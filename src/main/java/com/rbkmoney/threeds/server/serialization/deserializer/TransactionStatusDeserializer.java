package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class TransactionStatusDeserializer extends AbstractEnumDeserializer<TransactionStatus> {

    @Override
    protected TransactionStatus enumValueOf(String candidate) {
        return TransactionStatus.valueOf(candidate);
    }

    @Override
    protected TransactionStatus[] enumValues() {
        return TransactionStatus.values();
    }
}
