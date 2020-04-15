package com.rbkmoney.threeds.server.domain.whitelist;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Whitelist Status
 * Enables the communication of trusted
 * beneficiary/whitelist status between the ACS, the DS and the 3DS Requestor.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum WhiteListStatus implements Valuable {

    WHITELISTED("Y"),

    NOT_WHITELISTED("N"),

    NOT_ELIGIBLE("E"),

    PENDING("P"),

    REJECTED("R"),

    UNKNOWN("U");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
