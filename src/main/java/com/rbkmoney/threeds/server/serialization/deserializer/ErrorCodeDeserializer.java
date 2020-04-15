package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ErrorCodeDeserializer extends AbstractEnumDeserializer<ErrorCode> {

    @Override
    protected ErrorCode enumValueOf(String candidate) {
        return ErrorCode.valueOf(candidate);
    }

    @Override
    protected ErrorCode[] enumValues() {
        return ErrorCode.values();
    }
}
