package com.rbkmoney.threeds.server.helper;

import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;

public class CardRangeHelper {

    public static CardRange cardRange(
            ActionInd actionInd,
            String startRange,
            String endRange) {
        EnumWrapper<ActionInd> action = new EnumWrapper<>();
        action.setValue(actionInd);

        CardRange cardRange = new CardRange();
        cardRange.setActionInd(action);
        cardRange.setStartRange(startRange);
        cardRange.setEndRange(endRange);

        return cardRange;
    }
}
