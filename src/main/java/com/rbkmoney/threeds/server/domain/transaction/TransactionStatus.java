package com.rbkmoney.threeds.server.domain.transaction;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Indicates whether a transaction qualifies as an authenticated transaction or account verification.
 * Note: The Final CRes message can contain only a value of Y or N.
 * Note: If the 3DS Requestor Challenge Indicator = 06 (No challenge requested; Data share only),
 * then a Transaction Status of C is not valid.
 * <p>
 * Length: 1 character
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum TransactionStatus implements Valuable {

    AUTHENTICATION_VERIFICATION_SUCCESSFUL("Y"),

    NOT_AUTHENTICATED_DENIED("N"),

    TECHNICAL_PROBLEM("U"),

    NOT_AUTHENTICATED_ATTEMPTS_PERFORMED("A"),

    CHALLENGE_REQUIRED("C"),

    CHALLENGE_REQUIRED_DECOUPLED_AUTH("D"),

    AUTHENTICATION_REJECTED("R"),

    INFORMATIONAL_ONLY("I");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
