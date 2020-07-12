package com.rbkmoney.threeds.server.config.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@RequiredArgsConstructor
public class JsonMapper {

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    public <T> T readFromFile(String fullPath, Class<T> valueType) throws IOException {
        return objectMapper.readValue(readStringFromFile(fullPath), valueType);
    }

    public <T> T readValue(String src, Class<T> valueType) throws IOException {
        return objectMapper.readValue(src, valueType);
    }

    public <T> T readValue(byte[] src, Class<T> valueType) throws IOException {
        return objectMapper.readValue(src, valueType);
    }

    public byte[] writeValueAsBytes(String json) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(json);
    }

    public String writeValueAsString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public String readStringFromFile(String fullPath) throws IOException {
        return IOUtils.toString(
                resourceLoader.getResource("classpath:__files/" + fullPath).getInputStream(),
                Charsets.UTF_8);
    }
}
