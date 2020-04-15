package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class WhiteListStatusDeserializer extends AbstractEnumDeserializer<WhiteListStatus> {

    @Override
    protected WhiteListStatus enumValueOf(String candidate) {
        return WhiteListStatus.valueOf(candidate);
    }

    @Override
    protected WhiteListStatus[] enumValues() {
        return WhiteListStatus.values();
    }
}
