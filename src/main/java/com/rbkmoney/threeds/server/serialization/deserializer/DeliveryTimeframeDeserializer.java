package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.DeliveryTimeframe;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class DeliveryTimeframeDeserializer extends AbstractEnumDeserializer<DeliveryTimeframe> {

    @Override
    protected DeliveryTimeframe enumValueOf(String candidate) {
        return DeliveryTimeframe.valueOf(candidate);
    }

    @Override
    protected DeliveryTimeframe[] enumValues() {
        return DeliveryTimeframe.values();
    }
}
