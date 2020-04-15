package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeDSRequestorChallengeIndDeserializer extends AbstractEnumDeserializer<ThreeDSRequestorChallengeInd> {

    @Override
    protected ThreeDSRequestorChallengeInd enumValueOf(String candidate) {
        return ThreeDSRequestorChallengeInd.valueOf(candidate);
    }

    @Override
    protected ThreeDSRequestorChallengeInd[] enumValues() {
        return ThreeDSRequestorChallengeInd.values();
    }
}
