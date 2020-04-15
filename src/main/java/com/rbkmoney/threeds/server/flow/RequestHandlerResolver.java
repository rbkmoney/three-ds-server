package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.handle.RequestHandler;

public interface RequestHandlerResolver {

    RequestHandler resolve(Message message);
}
