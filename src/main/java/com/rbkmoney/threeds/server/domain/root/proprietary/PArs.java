package com.rbkmoney.threeds.server.domain.root.proprietary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;
import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.acs.AcsRenderingType;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import lombok.*;

import java.util.List;

/**
 * proprietary Authentication Response
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class PArs extends Message {

    @ToString.Include
    private String threeDSServerTransID;
    private String p_messageVersion;
    @ToString.Include
    private TransactionStatus transStatus;
    private String dsReferenceNumber;
    private String acsReferenceNumber;
    @ToString.Include
    private String acsTransID;
    @ToString.Include
    private String dsTransID;
    private String authenticationValue;
    private AcsRenderingType acsRenderingType;
    private String acsOperatorID;
    private String acsSignedContent;
    private String acsURL;
    @ToString.Include
    private AuthenticationType authenticationType;
    @ToString.Include
    private AcsChallengeMandated acsChallengeMandated;
    private String eci;
    private List<MessageExtension> messageExtension;
    @ToString.Include
    private String sdkTransID;
    @ToString.Include
    private TransactionStatusReason transStatusReason;
    private String cardholderInfo;
    private Object broadInfo;
    private AcsDecConInd acsDecConInd;
    @ToString.Include
    private WhiteListStatus whiteListStatus;
    @ToString.Include
    private WhiteListStatusSource whiteListStatusSource;

}
