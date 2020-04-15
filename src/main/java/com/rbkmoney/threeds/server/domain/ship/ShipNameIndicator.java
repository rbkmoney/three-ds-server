package com.rbkmoney.threeds.server.domain.ship;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Shipping Name Indicator
 * Indicates if the Cardholder Name on the account is identical to the shipping Name used for this transaction.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ShipNameIndicator implements Valuable {

    ACCOUNT_NAME_IDENTICAL("01"),

    ACCOUNT_NAME_DIFFERENT("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
