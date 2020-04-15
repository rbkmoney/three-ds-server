package com.rbkmoney.threeds.server.domain.root.proprietary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.domain.*;
import com.rbkmoney.threeds.server.domain.account.AccountInfoWrapper;
import com.rbkmoney.threeds.server.domain.account.AccountType;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptionsWrapper;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import com.rbkmoney.threeds.server.domain.transaction.TransactionType;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * proprietary Authentication Request
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@CustomValidation
public class PArq extends Message {

    private String acctNumber;
    private String cardExpiryDate;
    @ToString.Include
    @JsonDeserialize(using = DeviceChannelDeserializer.class)
    private EnumWrapper<DeviceChannel> deviceChannel;
    @ToString.Include
    @JsonDeserialize(using = MessageCategoryDeserializer.class)
    private EnumWrapper<MessageCategory> messageCategory;
    private String p_messageVersion;
    @ToString.Include
    private String threeDSRequestorID;
    @ToString.Include
    private String threeDSRequestorName;
    @ToString.Include
    private String threeDSRequestorURL;
    private String acquirerBIN;
    private String acquirerMerchantID;
    @JsonDeserialize(using = AddressMatchDeserializer.class)
    private EnumWrapper<AddressMatch> addrMatch;
    @JsonUnwrapped(prefix = "bill")
    private Address billingAddress;
    private String browserAcceptHeader;
    @JsonDeserialize(using = BrowserColorDepthDeserializer.class)
    private EnumWrapper<BrowserColorDepth> browserColorDepth;
    private String browserIP;
    private Boolean browserJavaEnabled;
    private String browserLanguage;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTZ;
    private String browserUserAgent;
    private String cardholderName;
    private DeviceRenderOptionsWrapper deviceRenderOptions;
    private String email;
    private Phone homePhone;
    private String mcc;
    private String merchantCountryCode;
    private String merchantName;
    private Phone mobilePhone;
    private String purchaseAmount;
    private String purchaseCurrency;
    @JsonDeserialize(using = LocalDateTimeMinuteSecondDeserializer.class)
    private TemporalAccessorWrapper<LocalDateTime> purchaseDate;
    private String purchaseExponent;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> recurringExpiry;
    private String recurringFrequency;
    private String sdkAppID;
    //TODO тут короче надо разбираться с шифрованием для приложения (возможно нам не надо)
    private String sdkEncData;
    //TODO тут короче надо разбираться с шифрованием для приложения (возможно нам не надо)
    private Object sdkEphemPubKey;
    private String sdkReferenceNumber;
    @ToString.Include
    private String sdkTransID;
    @JsonUnwrapped(prefix = "ship")
    private Address shippingAddress;
    @ToString.Include
    @JsonDeserialize(using = TransactionTypeDeserializer.class)
    private EnumWrapper<TransactionType> transType;
    private Phone workPhone;
    private String acctID;
    private AccountInfoWrapper acctInfo;
    @ToString.Include
    @JsonDeserialize(using = AccountTypeDeserializer.class)
    private EnumWrapper<AccountType> acctType;
    private MerchantRiskIndicatorWrapper merchantRiskIndicator;
    @JsonDeserialize(using = MessageExtensionDeserializer.class)
    private ListWrapper<MessageExtension> messageExtension;
    private Boolean payTokenInd;
    private String purchaseInstalData;
    private ThreeDSRequestorAuthenticationInfoWrapper threeDSRequestorAuthenticationInfo;
    @ToString.Include
    @JsonDeserialize(using = ThreeDSRequestorChallengeIndDeserializer.class)
    private EnumWrapper<ThreeDSRequestorChallengeInd> threeDSRequestorChallengeInd;
    @ToString.Include
    @JsonDeserialize(using = ThreeDSRequestorAuthenticationIndDeserializer.class)
    private EnumWrapper<ThreeDSRequestorAuthenticationInd> threeDSRequestorAuthenticationInd;
    @ToString.Include
    @JsonDeserialize(using = ThreeRIIndDeserializer.class)
    private EnumWrapper<ThreeRIInd> threeRIInd;
    @ToString.Include
    private ThreeDSRequestorPriorAuthenticationInfoWrapper threeDSRequestorPriorAuthenticationInfo;
    private String threeDSServerRefNumber;
    private String threeDSServerOperatorID;
    @ToString.Include
    private String threeDSServerTransID;
    @ToString.Include
    private String threeDSServerURL;
    private Object broadInfo;
    private String notificationURL;
    @ToString.Include
    @JsonDeserialize(using = ThreeDsMethodCompletionIndicatorDeserializer.class)
    private EnumWrapper<ThreeDsMethodCompletionIndicator> threeDSCompInd;
    @ToString.Include
    private String sdkMaxTimeout;
    private String acsURL;
    @ToString.Include
    private String threeDSRequestorDecMaxTime;
    @ToString.Include
    @JsonDeserialize(using = ThreeDSRequestorDecReqIndDeserializer.class)
    private EnumWrapper<ThreeDSRequestorDecReqInd> threeDSRequestorDecReqInd;
    private Boolean browserJavascriptEnabled;
    @JsonDeserialize(using = PayTokenSourceDeserializer.class)
    private EnumWrapper<PayTokenSource> payTokenSource;
    @ToString.Include
    @JsonDeserialize(using = WhiteListStatusDeserializer.class)
    private EnumWrapper<WhiteListStatus> whiteListStatus;
    @ToString.Include
    @JsonDeserialize(using = WhiteListStatusSourceDeserializer.class)
    private EnumWrapper<WhiteListStatusSource> whiteListStatusSource;

}
