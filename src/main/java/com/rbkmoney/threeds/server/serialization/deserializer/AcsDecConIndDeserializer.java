package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AcsDecConIndDeserializer extends AbstractEnumDeserializer<AcsDecConInd> {

    @Override
    protected AcsDecConInd enumValueOf(String candidate) {
        return AcsDecConInd.valueOf(candidate);
    }

    @Override
    protected AcsDecConInd[] enumValues() {
        return AcsDecConInd.values();
    }
}
