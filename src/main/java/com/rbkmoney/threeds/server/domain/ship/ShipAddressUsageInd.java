package com.rbkmoney.threeds.server.domain.ship;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Shipping Address Usage Indicator
 * Indicates when the shipping address used for this transaction was first used with the 3DS Requestor.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ShipAddressUsageInd implements Valuable {

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
