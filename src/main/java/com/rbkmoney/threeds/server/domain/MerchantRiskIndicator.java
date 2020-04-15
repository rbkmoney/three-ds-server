package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class MerchantRiskIndicator {

    private String deliveryEmailAddress;
    private DeliveryTimeframe deliveryTimeframe;
    private String giftCardAmount;
    private String giftCardCount;
    private String giftCardCurr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate preOrderDate;
    private PreOrderPurchaseInd preOrderPurchaseInd;
    private ReorderItemsInd reorderItemsInd;
    private ShipIndicator shipIndicator;

}
