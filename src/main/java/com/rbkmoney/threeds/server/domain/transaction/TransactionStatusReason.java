package com.rbkmoney.threeds.server.domain.transaction;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Transaction Status Reason
 * Provides information on why the Transaction Status field has the specified value.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum TransactionStatusReason implements Valuable {

    CARD_AUTH_FAILED("01"),

    UNKNOWN_DEVICE("02"),

    UNSUPPORTED_DEVICE("03"),

    EXCEEDS_AUTH_FREQUENCY_LIMIT("04"),

    EXPIRED_CARD("05"),

    INVALID_CARD_NUMBER("06"),

    INVALID_TRANSACTION("07"),

    NO_CARD_RECORD("08"),

    SECURITY_FAILURE("09"),

    STOLEN_CARD("10"),

    SUSPECTED_FRAUD("11"),

    TRANSACTION_NOT_PERMITTED("12"),

    CARDHOLDER_NOT_ENROLLED("13"),

    TRANSACTION_TIMED_OUT_AT_ACS("14"),

    LOW_CONFIDENCE("15"),

    MEDIUM_CONFIDENCE("16"),

    HIGH_CONFIDENCE("17"),

    VERY_HIGH_CONFIDENCE("18"),

    EXCEEDS_ACS_MAXIMUM_CHALLENGES("19"),

    NON_PAYMENT_TRANSACTION_NOT_SUPPORTED("20"),

    THREE_RI_TRANSACTION_NOT_SUPPORTED("21"),

    ACS_TECHNICAL_ISSUE("22"),

    DECOUPLED_AUTH_REQUIRED_BUT_NOT_REQUESTED("23"),

    REQUESTOR_DECOUPLED_MAX_EXPIRY_TIME_EXCEEDED("24"),

    INSUFFITIENT_TIME_DECOUPLED_AUTH("25"),

    AUTH_ATTEMPTED_BUT_NOT_PERFORMED("26");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    public boolean isReservedValueForNotRelevantMessageVersion() {
        return value.equals(ACS_TECHNICAL_ISSUE.getValue())
                || value.equals(DECOUPLED_AUTH_REQUIRED_BUT_NOT_REQUESTED.getValue())
                || value.equals(REQUESTOR_DECOUPLED_MAX_EXPIRY_TIME_EXCEEDED.getValue())
                || value.equals(INSUFFITIENT_TIME_DECOUPLED_AUTH.getValue())
                || value.equals(AUTH_ATTEMPTED_BUT_NOT_PERFORMED.getValue());
    }
}
