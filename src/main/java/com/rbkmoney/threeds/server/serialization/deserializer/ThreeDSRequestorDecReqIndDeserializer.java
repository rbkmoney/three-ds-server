package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorDecReqInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeDSRequestorDecReqIndDeserializer extends AbstractEnumDeserializer<ThreeDSRequestorDecReqInd> {

    @Override
    protected ThreeDSRequestorDecReqInd enumValueOf(String candidate) {
        return ThreeDSRequestorDecReqInd.valueOf(candidate);
    }

    @Override
    protected ThreeDSRequestorDecReqInd[] enumValues() {
        return ThreeDSRequestorDecReqInd.values();
    }
}
