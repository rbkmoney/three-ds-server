package com.rbkmoney.threeds.server.domain.ship;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Shipping Indicator
 * Indicates shipping method chosen for the transaction.
 * Merchants must choose the Shipping Indicator code that most
 * accurately describes the cardholder’s specific transaction, not
 * their general business.
 * If one or more items are included in the sale, use the Shipping
 * Indicator code for the physical goods, or if all digital goods,
 * use the Shipping Indicator code that describes the most
 * expensive item.
 */
@RequiredArgsConstructor
public enum ShipIndicator implements Valuable {

    CARDHOLDER_BILLING_ADDRESS("01"),

    ANOTHER_VERIFIED_ADDRESS("02"),

    DIFFERENT_FROM_CARDHOLDER_BILLING_ADDRESS("03"),

    SHIP_TO_STORE("04"),

    DIGITAL_GOODS("05"),

    TRAVEL_AND_EVENT_NOT_SHIPPED("06"),

    OTHER_NOT_SHIPPED("07");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
