package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.DsRequestHandlerResolver;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DsRequestHandleService {

    private final DsRequestHandlerResolver handleResolver;

    public Message handle(Message message) {
        DsRequestHandler handler = handleResolver.resolve(message);
        return handler.handle(message);
    }
}
