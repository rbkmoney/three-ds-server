package com.rbkmoney.threeds.server.config.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;

@RequiredArgsConstructor
public class JsonMapper {

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public <T> T readFromFile(String fullPath, Class<T> valueType) {
        return objectMapper.readValue(readStringFromFile(fullPath), valueType);
    }

    @SneakyThrows
    public <T> T readValue(String src, Class<T> valueType) {
        return objectMapper.readValue(src, valueType);
    }

    @SneakyThrows
    public <T> T readValue(byte[] src, Class<T> valueType) {
        return objectMapper.readValue(src, valueType);
    }

    @SneakyThrows
    public byte[] writeValueAsBytes(String json) {
        return objectMapper.writeValueAsBytes(json);
    }

    @SneakyThrows
    public String writeValueAsString(Object o) {
        return objectMapper.writeValueAsString(o);
    }

    @SneakyThrows
    public String readStringFromFile(String fullPath) {
        return IOUtils.toString(
                resourceLoader.getResource("classpath:__files/" + fullPath).getInputStream(),
                Charsets.UTF_8);
    }
}
