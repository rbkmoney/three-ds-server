package com.rbkmoney.threeds.server.domain.message;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Message Category
 * Identifies the category of the message for a specific use case.
 */
@RequiredArgsConstructor
public enum MessageCategory implements Valuable {

    PAYMENT_AUTH("01"),

    NON_PAYMENT_AUTH("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
