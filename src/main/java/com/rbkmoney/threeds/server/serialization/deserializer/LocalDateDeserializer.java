package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.serialization.AbstractTemporalAccessorDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends AbstractTemporalAccessorDeserializer<LocalDate> {

    public LocalDateDeserializer() {
        super("yyyyMMdd");
    }

    @Override
    protected LocalDate parse(String candidate, DateTimeFormatter formatter) {
        return LocalDate.parse(candidate, formatter);
    }
}
