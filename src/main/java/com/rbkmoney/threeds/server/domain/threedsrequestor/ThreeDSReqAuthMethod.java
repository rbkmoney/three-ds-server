package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Requestor Authentication Method
 * Mechanism used by the Cardholder to authenticate to the 3DS Requestor.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeDSReqAuthMethod implements Valuable {

    NO_AUTH_OCCURED("01"),

    REQUESTOR_CREDENTIALS("02"),

    FEDERATED_ID("03"),

    ISSUER_CREDENTIALS("04"),

    THIRD_PARTY_AUTH("05"),

    FIDO_AUTH_UNSIGNED("06"),

    FIDO_AUTH_SIGNED("07"),

    SRC_ASSURANCE("08");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
