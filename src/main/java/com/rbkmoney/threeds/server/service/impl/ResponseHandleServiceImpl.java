package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.ResponseHandlerResolver;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import com.rbkmoney.threeds.server.service.ResponseHandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseHandleServiceImpl implements ResponseHandleService {

    private final ResponseHandlerResolver handleResolver;

    @Override
    public Message handle(Message message) {
        ResponseHandler handler = handleResolver.resolve(message);
        return handler.handle(message);
    }
}
