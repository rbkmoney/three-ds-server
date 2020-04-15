package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.PayTokenSource;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class PayTokenSourceDeserializer extends AbstractEnumDeserializer<PayTokenSource> {

    @Override
    protected PayTokenSource enumValueOf(String candidate) {
        return PayTokenSource.valueOf(candidate);
    }

    @Override
    protected PayTokenSource[] enumValues() {
        return PayTokenSource.values();
    }
}
