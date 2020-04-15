package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Method Completion Indicator
 * Indicates whether the 3DS Method successfully completed.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 * <p>
 * Values accepted:
 * •
 * •
 * •
 */
@RequiredArgsConstructor
public enum ThreeDsMethodCompletionIndicator implements Valuable {

    SUCCESSFULLY_COMPLETED("Y"),

    FAILED("N"),

    UNKNOWN("U");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
