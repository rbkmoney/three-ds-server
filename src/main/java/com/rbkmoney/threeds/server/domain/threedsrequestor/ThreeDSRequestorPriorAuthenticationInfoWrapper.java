package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.LocalDateTimeMinuteDeserializer;
import com.rbkmoney.threeds.server.serialization.deserializer.ThreeDSReqPriorAuthMethodDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 3DS Requestor Prior Transaction Authentication Information
 * Information about how the 3DS Requestor authenticated the cardholder as part of a previous 3DS transaction.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class ThreeDSRequestorPriorAuthenticationInfoWrapper {

    private String threeDSReqPriorAuthData;
    @JsonDeserialize(using = ThreeDSReqPriorAuthMethodDeserializer.class)
    private EnumWrapper<ThreeDSReqPriorAuthMethod> threeDSReqPriorAuthMethod;
    @JsonDeserialize(using = LocalDateTimeMinuteDeserializer.class)
    private TemporalAccessorWrapper<LocalDateTime> threeDSReqPriorAuthTimestamp;
    private String threeDSReqPriorRef;

}
