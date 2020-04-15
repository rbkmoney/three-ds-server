package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

/**
 * Indicates the status of the Results Request message from the 3DS Server to provide additional data to the ACS.
 * This will indicate if the message was successfully received for further processing or will be used
 * to provide more detail on why the Challenge could not be completed from the 3DS Client to the ACS.
 * <p>
 * Length: 2 characters
 * <p>
 * JSON Data Type: String
 */
@RequiredArgsConstructor
public enum ResultsStatus {

    RREQ_RECEIVED_FOR_FURTHER_PROCESSING("01"),

    CREQ_NOT_SENT_TO_ACS("02"),

    ARES_NOT_DELIVERED_TO_THE_3DS_REQUESTOR("03");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
