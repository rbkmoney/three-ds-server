package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ShipAddressUsageIndDeserializer extends AbstractEnumDeserializer<ShipAddressUsageInd> {

    @Override
    protected ShipAddressUsageInd enumValueOf(String candidate) {
        return ShipAddressUsageInd.valueOf(candidate);
    }

    @Override
    protected ShipAddressUsageInd[] enumValues() {
        return ShipAddressUsageInd.values();
    }
}
