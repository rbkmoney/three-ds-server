package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.BrowserColorDepth;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class BrowserColorDepthDeserializer extends AbstractEnumDeserializer<BrowserColorDepth> {

    @Override
    protected BrowserColorDepth enumValueOf(String candidate) {
        return BrowserColorDepth.valueOf(candidate);
    }

    @Override
    protected BrowserColorDepth[] enumValues() {
        return BrowserColorDepth.values();
    }
}
