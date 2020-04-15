package com.rbkmoney.threeds.server.domain.whitelist;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Whitelist Status Source
 * This data element will be populated by the system setting Whitelist Status.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum WhiteListStatusSource implements Valuable {

    THREE_DS_SERVER("01"),

    DS("02"),

    ACS("03");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
