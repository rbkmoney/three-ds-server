package com.rbkmoney.threeds.server.domain.account;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Suspicious Account Activity
 * Indicates whether the 3DS Requestor has experienced suspicious activity
 * (including previous fraud) on the cardholder account.
 */
@RequiredArgsConstructor
public enum SuspiciousAccActivity implements Valuable {

    NO_SUSPICIOUS_ACTIVITY_OBSERVED("01"),

    SUSPICIOUS_ACTIVITY_OBSERVED("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
