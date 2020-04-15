package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.ChallengeWindowSize;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ChallengeWindowSizeDeserializer extends AbstractEnumDeserializer<ChallengeWindowSize> {

    @Override
    protected ChallengeWindowSize enumValueOf(String candidate) {
        return ChallengeWindowSize.valueOf(candidate);
    }

    @Override
    protected ChallengeWindowSize[] enumValues() {
        return ChallengeWindowSize.values();
    }
}
