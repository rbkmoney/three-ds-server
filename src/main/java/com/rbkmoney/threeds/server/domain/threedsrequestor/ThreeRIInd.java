package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3RI Indicator.
 * Indicates the type of 3RI request.
 * This data element provides additional information to the ACS
 * to determine the best approach for handing a 3RI request.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeRIInd implements Valuable {

    RECURRING_TRANSACTION("01"),

    INSTALMENT_TRANSACTION("02"),

    ADD_CARD("03"),

    MAINTAIN_CARD_INFO("04"),

    ACCOUNT_VERIFICATION("05"),

    SPLIT_OR_DELAYED_SHIPMENT("06"),

    TOP_UP("07"),

    MAIL_ORDER("08"),

    TELEPHONE_ORDER("09"),

    WHITELIST_STATUS_CHECK("10"),

    OTHER_PAYMENT("11");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    public boolean isReservedValueForNotRelevantMessageVersion() {
        return value.equals(SPLIT_OR_DELAYED_SHIPMENT.getValue())
                || value.equals(TOP_UP.getValue())
                || value.equals(MAIL_ORDER.getValue())
                || value.equals(TELEPHONE_ORDER.getValue())
                || value.equals(WHITELIST_STATUS_CHECK.getValue())
                || value.equals(OTHER_PAYMENT.getValue());
    }
}
