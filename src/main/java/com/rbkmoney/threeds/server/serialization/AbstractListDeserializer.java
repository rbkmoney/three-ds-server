package com.rbkmoney.threeds.server.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListDeserializer<T> extends JsonDeserializer<ListWrapper<T>> {

    protected abstract Class<T> getClassType();

    @Override
    public ListWrapper<T> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        if (node.isNull()) {
            return null;
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;

            List<T> validValues = new ArrayList<>();
            for (JsonNode jsonNode : arrayNode) {
                validValues.add(oc.treeToValue(jsonNode, getClassType()));
            }

            ListWrapper<T> listWrapper = new ListWrapper<>();
            listWrapper.setValue(validValues);
            return listWrapper;
        } else {
            ListWrapper<T> listWrapper = new ListWrapper<>();
            listWrapper.setGarbageValue(oc.treeToValue(node, Object.class));
            return listWrapper;
        }
    }
}
