package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AcsChallengeMandatedDeserializer extends AbstractEnumDeserializer<AcsChallengeMandated> {

    @Override
    protected AcsChallengeMandated enumValueOf(String candidate) {
        return AcsChallengeMandated.valueOf(candidate);
    }

    @Override
    protected AcsChallengeMandated[] enumValues() {
        return AcsChallengeMandated.values();
    }
}
