package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeRIIndDeserializer extends AbstractEnumDeserializer<ThreeRIInd> {

    @Override
    protected ThreeRIInd enumValueOf(String candidate) {
        return ThreeRIInd.valueOf(candidate);
    }

    @Override
    protected ThreeRIInd[] enumValues() {
        return ThreeRIInd.values();
    }
}
