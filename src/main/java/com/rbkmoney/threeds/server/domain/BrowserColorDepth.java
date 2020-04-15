package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BrowserColorDepth implements Valuable {

    BIT_1("1"),
    BITS_4("4"),
    BITS_8("8"),
    BITS_15("15"),
    BITS_16("16"),
    BITS_24("24"),
    BITS_32("32"),
    BITS_48("48");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

}
