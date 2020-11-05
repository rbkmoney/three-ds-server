package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.damsel.three_ds_server_storage.Action;
import com.rbkmoney.damsel.three_ds_server_storage.Add;
import com.rbkmoney.damsel.three_ds_server_storage.Delete;
import com.rbkmoney.damsel.three_ds_server_storage.Modify;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.*;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static java.lang.Long.parseLong;

@Component
public class CardRangeConverter {

    private static final Map<ActionInd, Action> ACTION_MAP = Map.of(
            ADD_CARD_RANGE_TO_CACHE, Action.add_card_range(new Add()),
            DELETE_CARD_RANGE_FROM_CACHE, Action.delete_card_range(new Delete()),
            MODIFY_CARD_RANGE_DATA, Action.modify_card_range(new Modify())
    );

    public com.rbkmoney.damsel.three_ds_server_storage.CardRange toThrift(CardRange domain) {
        long startRange = parseLong(domain.getStartRange());
        long endRange = parseLong(domain.getEndRange());
        return new com.rbkmoney.damsel.three_ds_server_storage.CardRange(
                startRange,
                endRange,
                ACTION_MAP.get(getValue(domain.getActionInd())));
    }

    public com.rbkmoney.threeds.server.domain.rbkmoney.cardrange.RBKMoneyCardRange toRBKMoney(CardRange domain) {
        return com.rbkmoney.threeds.server.domain.rbkmoney.cardrange.RBKMoneyCardRange.builder()
                .startRange(domain.getStartRange())
                .endRange(domain.getEndRange())
                .actionInd(getValue(domain.getActionInd()))
                .build();
    }
}
