package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeDSRequestorAuthenticationIndDeserializer extends AbstractEnumDeserializer<ThreeDSRequestorAuthenticationInd> {

    @Override
    protected ThreeDSRequestorAuthenticationInd enumValueOf(String candidate) {
        return ThreeDSRequestorAuthenticationInd.valueOf(candidate);
    }

    @Override
    protected ThreeDSRequestorAuthenticationInd[] enumValues() {
        return ThreeDSRequestorAuthenticationInd.values();
    }
}
