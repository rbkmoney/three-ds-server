package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class MessageCategoryDeserializer extends AbstractEnumDeserializer<MessageCategory> {

    @Override
    protected MessageCategory enumValueOf(String candidate) {
        return MessageCategory.valueOf(candidate);
    }

    @Override
    protected MessageCategory[] enumValues() {
        return MessageCategory.values();
    }
}
