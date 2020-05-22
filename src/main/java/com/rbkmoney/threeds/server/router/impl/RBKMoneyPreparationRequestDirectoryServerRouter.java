package com.rbkmoney.threeds.server.router.impl;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.router.DirectoryServerRouter;
import org.springframework.stereotype.Service;

@Service
public class RBKMoneyPreparationRequestDirectoryServerRouter implements DirectoryServerRouter {

    @Override
    public DirectoryServerProvider route(Message message) {
        RBKMoneyPreparationRequest request = (RBKMoneyPreparationRequest) message;
        return DirectoryServerProvider.of(request.getProviderId());
    }
}
