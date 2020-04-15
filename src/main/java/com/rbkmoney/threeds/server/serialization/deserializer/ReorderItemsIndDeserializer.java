package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ReorderItemsIndDeserializer extends AbstractEnumDeserializer<ReorderItemsInd> {

    @Override
    protected ReorderItemsInd enumValueOf(String candidate) {
        return ReorderItemsInd.valueOf(candidate);
    }

    @Override
    protected ReorderItemsInd[] enumValues() {
        return ReorderItemsInd.values();
    }
}
