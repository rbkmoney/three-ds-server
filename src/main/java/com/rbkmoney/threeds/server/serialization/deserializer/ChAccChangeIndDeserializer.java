package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.account.ChAccChangeInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ChAccChangeIndDeserializer extends AbstractEnumDeserializer<ChAccChangeInd> {

    @Override
    protected ChAccChangeInd enumValueOf(String candidate) {
        return ChAccChangeInd.valueOf(candidate);
    }

    @Override
    protected ChAccChangeInd[] enumValues() {
        return ChAccChangeInd.values();
    }
}
