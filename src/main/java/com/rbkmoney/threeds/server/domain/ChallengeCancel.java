package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * Indicator informing the ACS and the DS that the authentication has been canceled.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ChallengeCancel implements Valuable {

    CARDHOLDER_SELECTED_CANCEL("01"),

    TRANSACTION_TIMED_OUT_DECOUPLED_AUTH("03"),

    TRANSACTION_TIMED_OUT_OTHER_TIMEOUTS("04"),

    TRANSACTION_TIMED_OUT_FIRST_CREQ_NOT_RECEIVED("05"),

    TRANSACTION_ERROR("06"),

    UNKNOWN("07"),

    TRANSACTION_TIMED_OUT_AT_SDK("08");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    public boolean isReservedValueForNotRelevantMessageVersion() {
        return value.equals(TRANSACTION_TIMED_OUT_DECOUPLED_AUTH.getValue());
    }
}
