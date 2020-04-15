package com.rbkmoney.threeds.server.domain;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * Indicates the action to take with the card range. The card ranges are processed in the order returned.
 * <p>
 * Note: If the Serial Number is not included in the PReq message, then the action is A = Add for all card ranges
 * returned (the Action Indicator is ignored in the PRes message).
 * <p>
 * Note: M (Modify the card range data) is used only to modify or update data associated with the card ranges,
 * not to modify the Start Range and End Range.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ActionInd implements Valuable {

    ADD_CARD_RANGE_TO_CACHE("A"),

    DELETE_CARD_RANGE_FROM_CACHE("D"),

    MODIFY_CARD_RANGE_DATA("M");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
