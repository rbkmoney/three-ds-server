package com.rbkmoney.threeds.server.domain.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Code indicating the 3-D Secure component that identified the error.
 */
@RequiredArgsConstructor
public enum ErrorComponent implements Valuable {

    THREE_DS_SDK("C"),

    THREE_DS_SERVER("S"),

    DS("D"),

    ACS("A");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
