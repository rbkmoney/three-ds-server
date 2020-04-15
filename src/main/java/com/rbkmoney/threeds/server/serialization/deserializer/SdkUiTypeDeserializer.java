package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.sdk.SdkUiType;
import com.rbkmoney.threeds.server.serialization.AbstractListEnumDeserializer;

public class SdkUiTypeDeserializer extends AbstractListEnumDeserializer<SdkUiType> {

    @Override
    protected SdkUiType enumValueOf(String candidate) {
        return SdkUiType.valueOf(candidate);
    }

    @Override
    protected SdkUiType[] enumValues() {
        return SdkUiType.values();
    }
}
