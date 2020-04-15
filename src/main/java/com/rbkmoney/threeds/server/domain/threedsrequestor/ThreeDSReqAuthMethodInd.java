package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * 3DS Requestor Authentication Method Verification Indicator
 * Value that represents the signature verification performed by the DS on the mechanism
 * (e.g., FIDO) used by the cardholder to authenticate to the 3DS Requestor.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ThreeDSReqAuthMethodInd {

    VERIFIED("01"),

    FAILED("02"),

    NOT_PERFORMED("03");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
