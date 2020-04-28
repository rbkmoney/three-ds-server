package com.rbkmoney.threeds.server.router.impl;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.router.DirectoryServerRouter;
import org.springframework.stereotype.Service;

@Service
public class PPrqDirectoryServerRouter implements DirectoryServerRouter {

    @Override
    public DirectoryServerProvider route(Message message) {
        PPrq pPrq = (PPrq) message;

        if (pPrq.getXULTestCaseRunId() != null) {
            return DirectoryServerProvider.TEST;
        }

        // TODO [a.romanov]: PPrq is a message from our scheduling service. Route using messageExtension?
        return DirectoryServerProvider.TEST;
    }
}
