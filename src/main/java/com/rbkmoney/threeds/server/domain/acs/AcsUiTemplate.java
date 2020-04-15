package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * ACS UI Template
 * Identifies the UI Template format that the ACS first presents to the consumer.
 */
@RequiredArgsConstructor
public enum AcsUiTemplate implements Valuable {

    TEXT("01"),

    SINGLE_SELECT("02"),

    MULTI_SELECT("03"),

    OOB("04"),

    HTML_OTHER("05");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
