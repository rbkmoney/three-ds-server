package com.rbkmoney.threeds.server.router;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;

public interface DirectoryServerRouter {

    DirectoryServerProvider route(Message message);

}
