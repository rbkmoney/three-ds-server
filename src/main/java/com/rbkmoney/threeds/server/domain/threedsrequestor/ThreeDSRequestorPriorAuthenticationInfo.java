package com.rbkmoney.threeds.server.domain.threedsrequestor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ThreeDSRequestorPriorAuthenticationInfo {

    private String threeDSReqPriorAuthData;
    private ThreeDSReqPriorAuthMethod threeDSReqPriorAuthMethod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
    private LocalDateTime threeDSReqPriorAuthTimestamp;
    private String threeDSReqPriorRef;

}
