package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSReqPriorAuthMethod;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeDSReqPriorAuthMethodDeserializer extends AbstractEnumDeserializer<ThreeDSReqPriorAuthMethod> {

    @Override
    protected ThreeDSReqPriorAuthMethod enumValueOf(String candidate) {
        return ThreeDSReqPriorAuthMethod.valueOf(candidate);
    }

    @Override
    protected ThreeDSReqPriorAuthMethod[] enumValues() {
        return ThreeDSReqPriorAuthMethod.values();
    }
}
