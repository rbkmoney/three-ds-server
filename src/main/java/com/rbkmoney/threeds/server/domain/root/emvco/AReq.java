package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rbkmoney.threeds.server.domain.*;
import com.rbkmoney.threeds.server.domain.account.AccountInfo;
import com.rbkmoney.threeds.server.domain.account.AccountType;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.device.DeviceRenderOptions;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import com.rbkmoney.threeds.server.domain.transaction.TransactionType;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class AReq extends Message {

    @ToString.Include
    private ThreeDsMethodCompletionIndicator threeDSCompInd;
    @ToString.Include
    private ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd;
    private ThreeDSRequestorAuthenticationInfo threeDSRequestorAuthenticationInfo;
    @ToString.Include
    private ThreeDSReqAuthMethodInd threeDSReqAuthMethodInd;
    @ToString.Include
    private ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd;
    @ToString.Include
    private String threeDSRequestorDecMaxTime;
    @ToString.Include
    private ThreeDSRequestorDecReqInd threeDSRequestorDecReqInd;
    @ToString.Include
    private String threeDSRequestorID;
    @ToString.Include
    private String threeDSRequestorName;
    @ToString.Include
    private ThreeDSRequestorPriorAuthenticationInfo threeDSRequestorPriorAuthenticationInfo;
    private String threeDSRequestorURL;
    @ToString.Include
    private String threeDSServerRefNumber;
    @ToString.Include
    private String threeDSServerOperatorID;
    @ToString.Include
    private String threeDSServerTransID;
    @ToString.Include
    private String threeDSServerURL;
    @ToString.Include
    private ThreeRIInd threeRIInd;
    @ToString.Include
    private AccountType acctType;
    private String acquirerBIN;
    private String acquirerMerchantID;
    private AddressMatch addrMatch;
    private Object broadInfo;
    private String browserAcceptHeader;
    private String browserIP;
    private Boolean browserJavaEnabled;
    private Boolean browserJavascriptEnabled;
    private String browserLanguage;
    private BrowserColorDepth browserColorDepth;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTZ;
    private String browserUserAgent;
    private String cardExpiryDate;
    private AccountInfo acctInfo;
    private String acctNumber;
    private String acctID;
    @JsonUnwrapped(prefix = "bill")
    private Address billingAddress;
    private String email;
    private Phone homePhone;
    private Phone mobilePhone;
    private String cardholderName;
    @JsonUnwrapped(prefix = "ship")
    private Address shippingAddress;
    private Phone workPhone;
    @ToString.Include
    private DeviceChannel deviceChannel;
    private String deviceInfo;
    private DeviceRenderOptions deviceRenderOptions;
    private String dsReferenceNumber;
    @ToString.Include
    private String dsTransID;
    private String dsURL;
    private Boolean payTokenInd;
    private PayTokenSource payTokenSource;
    private String purchaseInstalData;
    private String mcc;
    private String merchantCountryCode;
    private String merchantName;
    @ToString.Include
    private MerchantRiskIndicator merchantRiskIndicator;
    @ToString.Include
    private MessageCategory messageCategory;
    private List<MessageExtension> messageExtension;
    private String notificationURL;
    private String purchaseAmount;
    private String purchaseCurrency;
    private String purchaseExponent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    private LocalDateTime purchaseDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate recurringExpiry;
    private String recurringFrequency;
    private String sdkAppID;
    //TODO тут короче надо разбираться с шифрованием для приложения (возможно нам не надо)
    private String sdkEncData;
    //TODO тут короче надо разбираться с шифрованием для приложения (возможно нам не надо)
    private Object sdkEphemPubKey;
    private String sdkMaxTimeout;
    private String sdkReferenceNumber;
    @ToString.Include
    private String sdkTransID;
    @ToString.Include
    private TransactionType transType;
    @ToString.Include
    private WhiteListStatus whiteListStatus;
    @ToString.Include
    private WhiteListStatusSource whiteListStatusSource;

    @JsonIgnore
    private LocalDateTime decoupledAuthMaxTime;

}
