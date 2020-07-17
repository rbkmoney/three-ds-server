package com.rbkmoney.threeds.server.ds.router.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.router.DsProviderRouter;
import org.springframework.stereotype.Service;

@Service
public class RBKMoneyPreparationRequestDsProviderRouter implements DsProviderRouter {

    @Override
    public DsProvider route(Message message) {
        RBKMoneyPreparationRequest request = (RBKMoneyPreparationRequest) message;
        return DsProvider.of(request.getProviderId());
    }
}
