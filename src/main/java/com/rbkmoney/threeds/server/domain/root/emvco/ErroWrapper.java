package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.error.ErrorComponent;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.ErrorCodeDeserializer;
import com.rbkmoney.threeds.server.serialization.deserializer.ErrorComponentDeserializer;
import lombok.*;

/**
 * Error message
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@CustomValidation
public class ErroWrapper extends Message {

    @JsonDeserialize(using = ErrorCodeDeserializer.class)
    private EnumWrapper<ErrorCode> errorCode;
    @JsonDeserialize(using = ErrorComponentDeserializer.class)
    private EnumWrapper<ErrorComponent> errorComponent;
    private String errorDescription;
    private String errorDetail;
    private String errorMessageType;
    private String sdkTransID;
    private String threeDSServerTransID;
    private String dsTransID;
    private String acsTransID;

}
