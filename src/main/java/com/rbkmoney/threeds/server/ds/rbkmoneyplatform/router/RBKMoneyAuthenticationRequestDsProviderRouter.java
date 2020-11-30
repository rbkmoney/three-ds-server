package com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.RBKMoneyDsProviderRouter;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestDsProviderRouter implements RBKMoneyDsProviderRouter {

    private final RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    @Override
    public DsProvider route(Message message) {
        RBKMoneyAuthenticationRequest request = (RBKMoneyAuthenticationRequest) message;

        String acctNumber = request.getAcctNumber();

        return rbkMoneyCardRangesStorageService.getDsProvider(acctNumber).orElse(null);
    }
}
