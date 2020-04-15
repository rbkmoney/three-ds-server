package com.rbkmoney.threeds.server.domain.device;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Device Channel
 * Indicates the type of channel interface being
 * used to initiate the transaction.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum DeviceChannel implements Valuable {

    APP_BASED("01"),

    BROWSER("02"),

    THREE_REQUESTOR_INITIATED("03");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
