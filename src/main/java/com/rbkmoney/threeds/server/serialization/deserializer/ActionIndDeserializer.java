package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ActionIndDeserializer extends AbstractEnumDeserializer<ActionInd> {

    @Override
    protected ActionInd enumValueOf(String candidate) {
        return ActionInd.valueOf(candidate);
    }

    @Override
    protected ActionInd[] enumValues() {
        return ActionInd.values();
    }
}
