package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.acs.AcsInfoInd;
import com.rbkmoney.threeds.server.serialization.AbstractListEnumDeserializer;

public class AcsInfoIndDeserializer extends AbstractListEnumDeserializer<AcsInfoInd> {

    @Override
    protected AcsInfoInd enumValueOf(String candidate) {
        return AcsInfoInd.valueOf(candidate);
    }

    @Override
    protected AcsInfoInd[] enumValues() {
        return AcsInfoInd.values();
    }
}
