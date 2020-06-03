package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.ResponseHandlerResolver;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseHandleService {

    private final ResponseHandlerResolver handleResolver;

    public Message handle(Message message) {
        ResponseHandler handler = handleResolver.resolve(message);
        return handler.handle(message);
    }
}
