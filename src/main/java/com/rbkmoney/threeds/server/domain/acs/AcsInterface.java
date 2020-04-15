package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * ACS Interface
 * This the ACS interface that the challenge will present to the cardholder.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum AcsInterface implements Valuable {

    NATIVE_UI("01"),

    HTML_UI("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
