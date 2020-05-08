package com.rbkmoney.threeds.server.router.impl;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.router.DirectoryServerRouter;
import org.springframework.stereotype.Service;

@Service
public class TestDirectoryServerRouter implements DirectoryServerRouter {

    @Override
    public DirectoryServerProvider route(Message message) {
        return DirectoryServerProvider.TEST;
    }
}
