package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDsMethodCompletionIndicator;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class ThreeDsMethodCompletionIndicatorDeserializer extends AbstractEnumDeserializer<ThreeDsMethodCompletionIndicator> {

    @Override
    protected ThreeDsMethodCompletionIndicator enumValueOf(String candidate) {
        return ThreeDsMethodCompletionIndicator.valueOf(candidate);
    }

    @Override
    protected ThreeDsMethodCompletionIndicator[] enumValues() {
        return ThreeDsMethodCompletionIndicator.values();
    }
}
