package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.account.PaymentAccInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class PaymentAccIndDeserializer extends AbstractEnumDeserializer<PaymentAccInd> {

    @Override
    protected PaymentAccInd enumValueOf(String candidate) {
        return PaymentAccInd.valueOf(candidate);
    }

    @Override
    protected PaymentAccInd[] enumValues() {
        return PaymentAccInd.values();
    }
}
