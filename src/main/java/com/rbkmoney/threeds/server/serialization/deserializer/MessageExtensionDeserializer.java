package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.serialization.AbstractListDeserializer;

public class MessageExtensionDeserializer extends AbstractListDeserializer<MessageExtension> {

    @Override
    protected Class<MessageExtension> getClassType() {
        return MessageExtension.class;
    }
}
