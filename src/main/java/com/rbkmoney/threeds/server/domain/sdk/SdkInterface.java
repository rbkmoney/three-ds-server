package com.rbkmoney.threeds.server.domain.sdk;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * SDK Interface.
 * Lists all of the SDK Interface types that the device supports for
 * displaying specific challenge user interfaces within the SDK.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum SdkInterface implements Valuable {

    NATIVE("01"),

    HTML("02"),

    BOTH("03");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
