package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.sdk.SdkInterface;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class SdkInterfaceDeserializer extends AbstractEnumDeserializer<SdkInterface> {

    @Override
    protected SdkInterface enumValueOf(String candidate) {
        return SdkInterface.valueOf(candidate);
    }

    @Override
    protected SdkInterface[] enumValues() {
        return SdkInterface.values();
    }
}
