package com.rbkmoney.threeds.server.domain.sdk;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * SDK UI Type
 * Lists all UI types that the device supports for displaying specific
 * challenge user interfaces within the SDK.
 * Valid enumValues for each Interface:
 * • Native UI = 01–04
 * • HTML UI = 01–05
 * Note: Currently, all SDKs need to support all UI Types. In the
 * future, however, this may change (for example, smart watches
 * may support a UI Type not yet defined by this specification).
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: Array of String
 */
@RequiredArgsConstructor
public enum SdkUiType implements Valuable {

    TEXT("01"),

    SINGLE_SELECT("02"),

    MULTI_SELECT("03"),

    OOB("04"),

    HTML_OTHER("05");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
