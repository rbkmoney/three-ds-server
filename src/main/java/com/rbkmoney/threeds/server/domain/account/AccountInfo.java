package com.rbkmoney.threeds.server.domain.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
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
public class AccountInfo {

    private ChAccAgeInd chAccAgeInd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate chAccChange;
    private ChAccChangeInd chAccChangeInd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate chAccDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate chAccPwChange;
    private ChAccPwChangeInd chAccPwChangeInd;
    private String nbPurchaseAccount;
    private String provisionAttemptsDay;
    private String txnActivityDay;
    private String txnActivityYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate paymentAccAge;
    private PaymentAccInd paymentAccInd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate shipAddressUsage;
    private ShipAddressUsageInd shipAddressUsageInd;
    private ShipNameIndicator shipNameIndicator;
    private SuspiciousAccActivity suspiciousAccActivity;

}
