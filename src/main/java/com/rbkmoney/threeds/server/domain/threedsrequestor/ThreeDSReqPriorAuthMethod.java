package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Requestor Prior Transaction Authentication Method
 * <p>
 * Mechanism used by the Cardholder to previously authenticate to the 3DS Requestor.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeDSReqPriorAuthMethod implements Valuable {

    FRICTIONLESS_AUTH_OCCURED("01"),

    CARDHOLDER_CHALLENGE_OCCURED("02"),

    AVS_VERIFIED("03"),

    OTHER_METHODS("04");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
