package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.authentication.AuthenticationMethod;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AuthenticationMethodDeserializer extends AbstractEnumDeserializer<AuthenticationMethod> {

    @Override
    protected AuthenticationMethod enumValueOf(String candidate) {
        return AuthenticationMethod.valueOf(candidate);
    }

    @Override
    protected AuthenticationMethod[] enumValues() {
        return AuthenticationMethod.values();
    }
}
