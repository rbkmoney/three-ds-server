package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class TransactionStatusReasonDeserializer extends AbstractEnumDeserializer<TransactionStatusReason> {

    @Override
    protected TransactionStatusReason enumValueOf(String candidate) {
        return TransactionStatusReason.valueOf(candidate);
    }

    @Override
    protected TransactionStatusReason[] enumValues() {
        return TransactionStatusReason.values();
    }
}
