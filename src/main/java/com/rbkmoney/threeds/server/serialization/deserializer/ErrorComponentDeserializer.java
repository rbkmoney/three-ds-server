package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.error.ErrorComponent;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ErrorComponentDeserializer extends AbstractEnumDeserializer<ErrorComponent> {

    @Override
    protected ErrorComponent enumValueOf(String candidate) {
        return ErrorComponent.valueOf(candidate);
    }

    @Override
    protected ErrorComponent[] enumValues() {
        return ErrorComponent.values();
    }
}
