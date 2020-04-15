package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class PreOrderPurchaseIndDeserializer extends AbstractEnumDeserializer<PreOrderPurchaseInd> {

    @Override
    protected PreOrderPurchaseInd enumValueOf(String candidate) {
        return PreOrderPurchaseInd.valueOf(candidate);
    }

    @Override
    protected PreOrderPurchaseInd[] enumValues() {
        return PreOrderPurchaseInd.values();
    }
}
