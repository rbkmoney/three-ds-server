package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryTimeframe implements Valuable {

    ELECTRONIC_DELIVERY("01"),

    SAME_DAY_SHIPPING("02"),

    OVERNIGHT_SHIPPING("03"),

    TWO_DAY_OR_MORE("04");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
