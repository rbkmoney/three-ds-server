package com.rbkmoney.threeds.server.domain.account;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Indicates the type of account. For example, for a multi-account card product.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum AccountType implements Valuable {

    NOT_APPLICABLE("01"),

    CREDIT("02"),

    DEBIT("03");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
