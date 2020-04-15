package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ShipNameIndicatorDeserializer extends AbstractEnumDeserializer<ShipNameIndicator> {

    @Override
    protected ShipNameIndicator enumValueOf(String candidate) {
        return ShipNameIndicator.valueOf(candidate);
    }

    @Override
    protected ShipNameIndicator[] enumValues() {
        return ShipNameIndicator.values();
    }
}
