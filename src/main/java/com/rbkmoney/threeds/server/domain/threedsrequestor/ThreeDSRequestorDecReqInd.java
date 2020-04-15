package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Requestor Decoupled Request Indicator
 * Indicates whether the 3DS Requestor requests the ACS to utilise Decoupled Authentication and agrees
 * to utilise Decoupled Authentication if the ACS confirms its use.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeDSRequestorDecReqInd implements Valuable {

    DECOUPLED_AUTH_IS_PREFFERED("Y"),

    DO_NOT_USE_DECOUPLED_AUTH("N"); //default

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
