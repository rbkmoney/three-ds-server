package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * ACS Challenge Mandated Indicator
 * Indication of whether a challenge is required for the transaction to be
 * authorised due to local/regional mandates or other variable.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum AcsChallengeMandated implements Valuable {

    CHALLENGE_MANDATED("Y"),

    CHALLENGE_IS_NOT_MANDATED("N");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
