package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.account.ChAccPwChangeInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ChAccPwChangeIndDeserializer extends AbstractEnumDeserializer<ChAccPwChangeInd> {

    @Override
    protected ChAccPwChangeInd enumValueOf(String candidate) {
        return ChAccPwChangeInd.valueOf(candidate);
    }

    @Override
    protected ChAccPwChangeInd[] enumValues() {
        return ChAccPwChangeInd.values();
    }
}
