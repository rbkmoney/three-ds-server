package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.error.ErrorComponent;
import com.rbkmoney.threeds.server.domain.root.Message;
import lombok.*;

/**
 * Error message
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Erro extends Message {

    @ToString.Include
    private ErrorCode errorCode;
    @ToString.Include
    private ErrorComponent errorComponent;
    @ToString.Include
    private String errorDescription;
    @ToString.Include
    private String errorDetail;
    @ToString.Include
    private String errorMessageType;
    @ToString.Include
    private String sdkTransID;
    @ToString.Include
    private String threeDSServerTransID;
    @ToString.Include
    private String dsTransID;
    @ToString.Include
    private String acsTransID;

    @JsonIgnore
    private boolean handleRepetitionNeeded = false;
    @JsonIgnore
    private boolean notifyDsAboutError = true;

}
