package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.*;
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
public class MerchantRiskIndicatorWrapper {

    private String deliveryEmailAddress;
    @JsonDeserialize(using = DeliveryTimeframeDeserializer.class)
    private EnumWrapper<DeliveryTimeframe> deliveryTimeframe;
    private String giftCardAmount;
    private String giftCardCount;
    private String giftCardCurr;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private TemporalAccessorWrapper<LocalDate> preOrderDate;
    @JsonDeserialize(using = PreOrderPurchaseIndDeserializer.class)
    private EnumWrapper<PreOrderPurchaseInd> preOrderPurchaseInd;
    @JsonDeserialize(using = ReorderItemsIndDeserializer.class)
    private EnumWrapper<ReorderItemsInd> reorderItemsInd;
    @JsonDeserialize(using = ShipIndicatorDeserializer.class)
    private EnumWrapper<ShipIndicator> shipIndicator;

}
