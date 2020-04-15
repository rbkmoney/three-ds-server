package com.rbkmoney.threeds.server.domain.authentication;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Indicates the type of authentication method the Issuer will use to challenge the Cardholder,
 * whether in the ARes message or what was used by the ACS when in the RReq message.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum AuthenticationType implements Valuable {

    STATIC("01"),

    DYNAMIC("02"),

    OOB("03"),

    DECOUPLED("04");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
