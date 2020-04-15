package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.acs.AcsUiTemplate;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AcsUiTemplateDeserializer extends AbstractEnumDeserializer<AcsUiTemplate> {

    @Override
    protected AcsUiTemplate enumValueOf(String candidate) {
        return AcsUiTemplate.valueOf(candidate);
    }

    @Override
    protected AcsUiTemplate[] enumValues() {
        return AcsUiTemplate.values();
    }
}
