package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * EMV Payment Token Source
 * This data element will be populated by the system residing in the 3-D Secure
 * domain where the detokenisation occurs.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum PayTokenSource implements Valuable {

    THREE_DS_SERVER("01"),

    DS("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
