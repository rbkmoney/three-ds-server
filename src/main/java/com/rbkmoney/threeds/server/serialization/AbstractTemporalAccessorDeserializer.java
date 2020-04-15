package com.rbkmoney.threeds.server.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@RequiredArgsConstructor
public abstract class AbstractTemporalAccessorDeserializer<T extends TemporalAccessor> extends JsonDeserializer<TemporalAccessorWrapper<T>> {

    private final String pattern;

    protected abstract T parse(String candidate, DateTimeFormatter formatter);

    @Override
    public TemporalAccessorWrapper<T> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        if (node.isNull()) {
            return null;
        } else if (node.isTextual()) {
            TextNode textNode = (TextNode) node;
            String candidate = textNode.asText();

            T temporalAccessor = parse(candidate, DateTimeFormatter.ofPattern(pattern));

            TemporalAccessorWrapper<T> temporalAccessorWrapper = new TemporalAccessorWrapper<>();
            temporalAccessorWrapper.setValue(temporalAccessor);
            return temporalAccessorWrapper;
        } else {
            TemporalAccessorWrapper<T> temporalAccessorWrapper = new TemporalAccessorWrapper<>();
            temporalAccessorWrapper.setGarbageValue(oc.treeToValue(node, Object.class));
            return temporalAccessorWrapper;
        }
    }
}
