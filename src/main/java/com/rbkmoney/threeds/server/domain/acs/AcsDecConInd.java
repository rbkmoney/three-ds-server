package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * ACS Decoupled Confirmation Indicator
 * Indicates whether the ACS confirms utilisation of Decoupled Authentication
 * and agrees to utilise Decoupled Authentication to authenticate the Cardholder.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum AcsDecConInd implements Valuable {

    DECOUPLED_AUTH_WILL_BE_USED("Y"),

    DECOUPLED_AUTH_WILL_NOT_BE_USED("N");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
