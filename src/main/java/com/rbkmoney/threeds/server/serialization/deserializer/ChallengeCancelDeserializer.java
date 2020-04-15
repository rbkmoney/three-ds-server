package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.ChallengeCancel;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ChallengeCancelDeserializer extends AbstractEnumDeserializer<ChallengeCancel> {

    @Override
    protected ChallengeCancel enumValueOf(String candidate) {
        return ChallengeCancel.valueOf(candidate);
    }

    @Override
    protected ChallengeCancel[] enumValues() {
        return ChallengeCancel.values();
    }
}
