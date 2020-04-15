package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSReqAuthMethod;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeDSReqAuthMethodDeserializer extends AbstractEnumDeserializer<ThreeDSReqAuthMethod> {

    @Override
    protected ThreeDSReqAuthMethod enumValueOf(String candidate) {
        return ThreeDSReqAuthMethod.valueOf(candidate);
    }

    @Override
    protected ThreeDSReqAuthMethod[] enumValues() {
        return ThreeDSReqAuthMethod.values();
    }
}
