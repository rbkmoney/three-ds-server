package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.AddressMatch;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AddressMatchDeserializer extends AbstractEnumDeserializer<AddressMatch> {

    @Override
    protected AddressMatch enumValueOf(String candidate) {
        return AddressMatch.valueOf(candidate);
    }

    @Override
    protected AddressMatch[] enumValues() {
        return AddressMatch.values();
    }
}
