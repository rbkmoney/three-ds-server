package com.rbkmoney.threeds.server.domain.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Code indicating the type of problem identified in the message.
 */
@RequiredArgsConstructor
public enum ErrorCode implements Valuable {

    MESSAGE_RECEIVED_INVALID_101("101"),

    MESSAGE_VERSION_NOT_SUPPORTED_102("102"),

    SENT_MESSAGE_LIMIT_EXCEEDED_103("103"),

    REQUIRED_DATA_ELEMENT_MISSING_201("201"),

    CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED_202("202"),

    FORMAT_OF_DATA_ELEMENTS_INVALID_203("203"),

    DUPLICATE_DATA_ELEMENT_204("204"),

    TRANSACTION_ID_NOT_RECOGNISED_301("301"),

    DATA_DECRYPTION_FAILURE_302("302"),

    ACCESS_DENIED_OR_INVALID_ENDPOINT_303("303"),

    ISO_CODE_INVALID_304("304"),

    TRANSACTION_DATA_NOT_VALID_305("305"),

    MCC_NOT_VALID_FOR_PAYMENT_SYSTEM_306("306"),

    SERIAL_NUMBER_NOT_VALID_307("307"),

    TRANSACTION_TIMED_OUT_402("402"),

    TRANSIENT_SYSTEM_FAILURE_403("403"),

    PERMANENT_SYSTEM_FAILURE_404("404"),

    SYSTEM_CONNECTION_FAILURE_405("405");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
