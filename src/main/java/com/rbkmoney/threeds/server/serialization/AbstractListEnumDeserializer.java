package com.rbkmoney.threeds.server.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.rbkmoney.threeds.server.domain.Valuable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListEnumDeserializer<T extends Valuable> extends JsonDeserializer<ListWrapper<EnumWrapper<T>>> {

    protected abstract T enumValueOf(String candidate);

    protected abstract T[] enumValues();

    @Override
    public ListWrapper<EnumWrapper<T>> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        if (node.isNull()) {
            return null;
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            List<EnumWrapper<T>> validValues = new ArrayList<>();
            for (JsonNode jsonNode : arrayNode) {
                validValues.add(getEnumElementOfList(oc, jsonNode));
            }

            ListWrapper<EnumWrapper<T>> listWrapper = new ListWrapper<>();
            listWrapper.setValue(validValues);
            return listWrapper;
        } else {
            ListWrapper<EnumWrapper<T>> listWrapper = new ListWrapper<>();
            listWrapper.setGarbageValue(oc.treeToValue(node, Object.class));
            return listWrapper;
        }
    }

    private EnumWrapper<T> getEnumElementOfList(ObjectCodec oc, JsonNode node) throws IOException, JsonProcessingException {
        if (node.isNull()) {
            return null;
        } else if (node.isTextual()) {
            TextNode textNode = (TextNode) node;
            String candidate = textNode.asText();

            for (T value : enumValues()) {
                if (value.getValue().equals(candidate)) {
                    EnumWrapper<T> enumWrapper = new EnumWrapper<>();
                    enumWrapper.setValue(value);
                    return enumWrapper;
                }
            }

            EnumWrapper<T> enumWrapper = new EnumWrapper<>();
            enumWrapper.setGarbageValue(candidate);
            return enumWrapper;
        } else {
            EnumWrapper<T> enumWrapper = new EnumWrapper<>();
            enumWrapper.setGarbageValue(oc.treeToValue(node, Object.class));
            return enumWrapper;
        }
    }
}
