package com.rbkmoney.threeds.server.domain.authentication;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Authentication approach that the ACS used to authenticate the Cardholder for this specific transaction.
 * Note: This is in the RReq message from the ACS only. It is not passed to the 3DS Server URL.
 * Note: For 3RI, only present for Decoupled Authentication
 */
@RequiredArgsConstructor
public enum AuthenticationMethod implements Valuable {

    STATIC_PASSCODE("01"),

    SMS_OTP("02"),

    KEY_FOB_OR_EMV_CARD_READER_OTP("03"),

    APP_OTP("04"),

    OTP_OTHER("05"),

    KBA("06"),

    OOB_BIOMETRICS("07"),

    OOB_LOGIN("08"),

    OOB_OTHER("09"),

    OTHER("10"),

    PUSH_CONFIRMATION("11");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    public boolean isReservedValueForNotRelevantMessageVersion() {
        return value.equals(PUSH_CONFIRMATION.getValue());
    }
}
