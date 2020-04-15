package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.account.SuspiciousAccActivity;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class SuspiciousAccActivityDeserializer extends AbstractEnumDeserializer<SuspiciousAccActivity> {

    @Override
    protected SuspiciousAccActivity enumValueOf(String candidate) {
        return SuspiciousAccActivity.valueOf(candidate);
    }

    @Override
    protected SuspiciousAccActivity[] enumValues() {
        return SuspiciousAccActivity.values();
    }
}
