package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.RequestHandlerResolver;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestHandleService {

    private final RequestHandlerResolver handleResolver;

    public Message handle(Message message) {
        RequestHandler handler = handleResolver.resolve(message);
        return handler.handle(message);
    }
}
