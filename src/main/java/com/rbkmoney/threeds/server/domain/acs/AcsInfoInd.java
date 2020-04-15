package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AcsInfoInd implements Valuable {

    AUTH_AVAILABLE_AT_ACS("01"),

    ATTEMPTS_SUPPORTED_BY_ACS_OR_DS("02"),

    DECOUPLED_AUTH_SUPPORTED("03"),

    WHITELISTING_SUPPORTED("04");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
