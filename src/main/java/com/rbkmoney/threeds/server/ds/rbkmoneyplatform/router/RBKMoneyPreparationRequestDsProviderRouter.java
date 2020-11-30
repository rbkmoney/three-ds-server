package com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.RBKMoneyDsProviderRouter;

public class RBKMoneyPreparationRequestDsProviderRouter implements RBKMoneyDsProviderRouter {

    @Override
    public DsProvider route(Message message) {
        RBKMoneyPreparationRequest request = (RBKMoneyPreparationRequest) message;
        return DsProvider.of(request.getProviderId());
    }
}
