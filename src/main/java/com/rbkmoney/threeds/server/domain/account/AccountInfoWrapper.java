package com.rbkmoney.threeds.server.domain.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Cardholder Account Information.
 * Additional information about the Cardholder’s account provided by the 3DS Requestor.
 * <p>
 * JSON Data Type: Object
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class AccountInfoWrapper {

    @JsonDeserialize(using = ChAccAgeIndDeserializer.class)
    private EnumWrapper<ChAccAgeInd> chAccAgeInd;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> chAccChange;
    @JsonDeserialize(using = ChAccChangeIndDeserializer.class)
    private EnumWrapper<ChAccChangeInd> chAccChangeInd;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> chAccDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> chAccPwChange;
    @JsonDeserialize(using = ChAccPwChangeIndDeserializer.class)
    private EnumWrapper<ChAccPwChangeInd> chAccPwChangeInd;
    private String nbPurchaseAccount;
    private String provisionAttemptsDay;
    private String txnActivityDay;
    private String txnActivityYear;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> paymentAccAge;
    @JsonDeserialize(using = PaymentAccIndDeserializer.class)
    private EnumWrapper<PaymentAccInd> paymentAccInd;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> shipAddressUsage;
    @JsonDeserialize(using = ShipAddressUsageIndDeserializer.class)
    private EnumWrapper<ShipAddressUsageInd> shipAddressUsageInd;
    @JsonDeserialize(using = ShipNameIndicatorDeserializer.class)
    private EnumWrapper<ShipNameIndicator> shipNameIndicator;
    @JsonDeserialize(using = SuspiciousAccActivityDeserializer.class)
    private EnumWrapper<SuspiciousAccActivity> suspiciousAccActivity;

}
