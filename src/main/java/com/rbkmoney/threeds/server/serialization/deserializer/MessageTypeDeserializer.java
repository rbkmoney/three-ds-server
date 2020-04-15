package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.message.MessageType;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class MessageTypeDeserializer extends AbstractEnumDeserializer<MessageType> {

    @Override
    protected MessageType enumValueOf(String candidate) {
        return MessageType.valueOf(candidate);
    }

    @Override
    protected MessageType[] enumValues() {
        return MessageType.values();
    }
}
