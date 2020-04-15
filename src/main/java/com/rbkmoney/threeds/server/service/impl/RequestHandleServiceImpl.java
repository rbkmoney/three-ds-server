package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.RequestHandlerResolver;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestHandleServiceImpl implements RequestHandleService {

    private final RequestHandlerResolver handleResolver;

    @Override
    public Message handle(Message message) {
        RequestHandler handler = handleResolver.resolve(message);
        return handler.handle(message);
    }
}
