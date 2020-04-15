package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.acs.AcsInterface;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AcsInterfaceDeserializer extends AbstractEnumDeserializer<AcsInterface> {

    @Override
    protected AcsInterface enumValueOf(String candidate) {
        return AcsInterface.valueOf(candidate);
    }

    @Override
    protected AcsInterface[] enumValues() {
        return AcsInterface.values();
    }
}
