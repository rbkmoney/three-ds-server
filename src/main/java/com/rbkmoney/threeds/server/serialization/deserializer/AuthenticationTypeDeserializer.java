package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AuthenticationTypeDeserializer extends AbstractEnumDeserializer<AuthenticationType> {

    @Override
    protected AuthenticationType enumValueOf(String candidate) {
        return AuthenticationType.valueOf(candidate);
    }

    @Override
    protected AuthenticationType[] enumValues() {
        return AuthenticationType.values();
    }
}
