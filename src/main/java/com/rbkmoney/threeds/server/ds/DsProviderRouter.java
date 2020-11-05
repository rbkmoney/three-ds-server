package com.rbkmoney.threeds.server.ds;

import com.rbkmoney.threeds.server.domain.root.Message;

public interface DsProviderRouter {

    DsProvider route(Message message);

}
