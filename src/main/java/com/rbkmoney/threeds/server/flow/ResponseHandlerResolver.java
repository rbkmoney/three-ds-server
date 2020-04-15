package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.handle.ResponseHandler;

public interface ResponseHandlerResolver {

    ResponseHandler resolve(Message message);
}
