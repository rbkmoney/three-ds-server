package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * Indicates whether the Cardholder Shipping Address and Cardholder
 * Billing Address are the same.
 */
@RequiredArgsConstructor
public enum AddressMatch implements Valuable {

    SAME_ADDRESS("Y"),

    DIFFERENT_ADDRESS("N");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
