package com.rbkmoney.threeds.server.domain.order;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Reorder Items Indicator
 * Indicates whether the cardholder is reordering previously
 * purchased merchandise.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ReorderItemsInd implements Valuable {

    FIRST_TIME_ORDERED("01"),

    REORDERED("02");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
