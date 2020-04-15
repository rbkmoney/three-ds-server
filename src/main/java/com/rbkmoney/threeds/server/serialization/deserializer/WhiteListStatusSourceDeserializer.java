package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class WhiteListStatusSourceDeserializer extends AbstractEnumDeserializer<WhiteListStatusSource> {

    @Override
    protected WhiteListStatusSource enumValueOf(String candidate) {
        return WhiteListStatusSource.valueOf(candidate);
    }

    @Override
    protected WhiteListStatusSource[] enumValues() {
        return WhiteListStatusSource.values();
    }
}
