package com.rbkmoney.threeds.server.ds.router;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProvider;

public interface DsProviderRouter {

    DsProvider route(Message message);

}
