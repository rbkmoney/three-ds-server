package com.rbkmoney.threeds.server.domain.account;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Payment Account Age Indicator
 * Indicates the length of time that the payment account was enrolled
 * in the cardholder’s account with the 3DS Requestor.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum PaymentAccInd implements Valuable {

    NO_ACCOUNT("01"),

    DURING_THIS_TRANSACTION("02"),

    LESS_THEN_30_DAYS("03"),

    FROM_30_TO_60_DAYS("04"),

    MORE_THAN_60_DAYS("05");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
