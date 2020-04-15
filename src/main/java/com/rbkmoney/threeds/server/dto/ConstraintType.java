package com.rbkmoney.threeds.server.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ConstraintType {

    AUTH_DEC_TIME_IS_EXPIRED("AuthDecTimeIsExpired"),
    OUT_OF_CARD_RANGE("OutOfCardRange"),
    NOT_NULL("NotNull"),
    NOT_BLANK("NotBlank"),
    PATTERN("Pattern"),
    ID_NOT_RECOGNISED("IdNotRecognised"),
    CRITICAL_MESSAGE_EXTENSION_NOT_RECOGNISED("CriticalMessageExtensionNotRecognised");

    private final String value;

    public static ConstraintType of(String value) {
        for (ConstraintType t : ConstraintType.values()) {
            if (value.equals(t.value)) return t;
        }

        throw new IllegalArgumentException("Unknown constraint type: " + value);
    }
}
