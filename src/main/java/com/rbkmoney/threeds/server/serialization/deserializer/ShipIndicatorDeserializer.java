package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ShipIndicatorDeserializer extends AbstractEnumDeserializer<ShipIndicator> {

    @Override
    protected ShipIndicator enumValueOf(String candidate) {
        return ShipIndicator.valueOf(candidate);
    }

    @Override
    protected ShipIndicator[] enumValues() {
        return ShipIndicator.values();
    }
}
