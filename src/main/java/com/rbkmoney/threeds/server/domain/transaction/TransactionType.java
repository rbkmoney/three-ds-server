package com.rbkmoney.threeds.server.domain.transaction;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Transaction Type
 * Identifies the type of transaction being authenticated.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 * <p>
 * Values derived from the 8583 ISO Standard.
 */
@RequiredArgsConstructor
public enum TransactionType implements Valuable {

    GOODS_OR_SERVICE_PURCHASE("01"),

    CHECK_ACCEPTANCE("03"),

    ACCOUNT_FUNDING("10"),

    QUASI_CASH_TRANSACTION("11"),

    PREPAID_ACTIVATION_AND_LOAD("28");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
