package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.serialization.AbstractTemporalAccessorDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeMinuteDeserializer extends AbstractTemporalAccessorDeserializer<LocalDateTime> {

    public LocalDateTimeMinuteDeserializer() {
        super("yyyyMMddHHmm");
    }

    @Override
    protected LocalDateTime parse(String candidate, DateTimeFormatter formatter) {
        return LocalDateTime.parse(candidate, formatter);
    }
}
