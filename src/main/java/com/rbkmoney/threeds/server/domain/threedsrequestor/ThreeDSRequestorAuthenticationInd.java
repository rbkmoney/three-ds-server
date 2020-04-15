package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Requestor Authentication Indicator
 * <p>
 * Indicates the type of Authentication request.
 * This data element provides additional information to the ACS to determine the best
 * approach for handing an authentication request.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeDSRequestorAuthenticationInd implements Valuable {

    PAYMENT_TRANSACTION("01"),

    RECURRING_TRANSACTION("02"),

    INSTALMENT_TRANSACTION("03"),

    ADD_CARD("04"),

    MAINTAIN_CARD("05"),

    CARDHOLDER_VERIFICATION("06");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
