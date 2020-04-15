package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.account.ChAccAgeInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ChAccAgeIndDeserializer extends AbstractEnumDeserializer<ChAccAgeInd> {

    @Override
    protected ChAccAgeInd enumValueOf(String candidate) {
        return ChAccAgeInd.valueOf(candidate);
    }

    @Override
    protected ChAccAgeInd[] enumValues() {
        return ChAccAgeInd.values();
    }
}
