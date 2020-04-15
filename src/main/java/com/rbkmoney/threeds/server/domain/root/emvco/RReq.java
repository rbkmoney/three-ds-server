package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.domain.ChallengeCancel;
import com.rbkmoney.threeds.server.domain.acs.AcsRenderingTypeWrapper;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationMethod;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@CustomValidation
public class RReq extends Message {

    @ToString.Include
    private AcsRenderingTypeWrapper acsRenderingType;
    @ToString.Include
    @JsonDeserialize(using = AuthenticationMethodDeserializer.class)
    private EnumWrapper<AuthenticationMethod> authenticationMethod;
    @ToString.Include
    @JsonDeserialize(using = AuthenticationTypeDeserializer.class)
    private EnumWrapper<AuthenticationType> authenticationType;
    private String authenticationValue;
    @ToString.Include
    @JsonDeserialize(using = ChallengeCancelDeserializer.class)
    private EnumWrapper<ChallengeCancel> challengeCancel;
    private String eci;
    private String interactionCounter;
    @JsonDeserialize(using = MessageCategoryDeserializer.class)
    private EnumWrapper<MessageCategory> messageCategory;
    @JsonDeserialize(using = MessageExtensionDeserializer.class)
    private ListWrapper<MessageExtension> messageExtension;
    @ToString.Include
    @JsonDeserialize(using = TransactionStatusDeserializer.class)
    private EnumWrapper<TransactionStatus> transStatus;
    @ToString.Include
    @JsonDeserialize(using = TransactionStatusReasonDeserializer.class)
    private EnumWrapper<TransactionStatusReason> transStatusReason;
    @ToString.Include
    @JsonDeserialize(using = WhiteListStatusDeserializer.class)
    private EnumWrapper<WhiteListStatus> whiteListStatus;
    @ToString.Include
    @JsonDeserialize(using = WhiteListStatusSourceDeserializer.class)
    private EnumWrapper<WhiteListStatusSource> whiteListStatusSource;
    @ToString.Include
    private String sdkTransID;
    @ToString.Include
    private String threeDSServerTransID;
    @ToString.Include
    private String dsTransID;
    @ToString.Include
    private String acsTransID;

}
