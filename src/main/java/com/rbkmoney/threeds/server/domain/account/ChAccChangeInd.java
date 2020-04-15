package com.rbkmoney.threeds.server.domain.account;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;


/**
 * Length of time since the cardholder’s account information with the 3DS Requestor was last
 * changed, including Billing or Shipping address, new payment account, or new user(s) added.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ChAccChangeInd implements Valuable {

    THIS_TRANSACTION("01"),

    LESS_THEN_30_DAYS("02"),

    FROM_30_TO_60_DAYS("03"),

    MORE_THAN_60_DAYS("04");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
