package com.rbkmoney.threeds.server.domain.order;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Pre-Order Purchase Indicator
 * Indicates whether Cardholder is placing an order for
 * merchandise with a future availability or release date.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum PreOrderPurchaseInd implements Valuable {

    MERCHANDISE_AVAILABLE("01"),

    FUTURE_AVAILABILITY("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
