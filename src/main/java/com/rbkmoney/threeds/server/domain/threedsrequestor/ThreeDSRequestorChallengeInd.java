package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Requestor Challenge Indicator
 * Indicates whether a challenge is requested for this transaction.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeDSRequestorChallengeInd implements Valuable {

    NO_PREFERENCE("01"), //default

    NO_CHALLENGE("02"),

    CHALLENGE_REQUESTED_BY_REQUESTOR("03"),

    CHALLENGE_REQUESTED_MANDATE("04"),

    NO_CHALLENGE_RISK_ANALYSIS_PERFORMED("05"),

    NO_CHALLENGE_DATA_SHARE_ONLY("06"),

    NO_CHALLENGE_AUTH_ALREADY_PERFORMED("07"),

    NO_CHALLENGE_UTILISE_WHITELIST_EXEMPTION("08"),

    CHALLENGE_REQUESTED_WHITELIST_PROMPT_REQUESTED("09");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    public boolean isReservedValueForNotRelevantMessageVersion() {
        return value.equals(NO_CHALLENGE_RISK_ANALYSIS_PERFORMED.getValue())
                || value.equals(NO_CHALLENGE_DATA_SHARE_ONLY.getValue())
                || value.equals(NO_CHALLENGE_AUTH_ALREADY_PERFORMED.getValue())
                || value.equals(NO_CHALLENGE_UTILISE_WHITELIST_EXEMPTION.getValue())
                || value.equals(CHALLENGE_REQUESTED_WHITELIST_PROMPT_REQUESTED.getValue());
    }
}
