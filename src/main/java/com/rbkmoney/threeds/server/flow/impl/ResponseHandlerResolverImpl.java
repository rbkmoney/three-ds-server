package com.rbkmoney.threeds.server.flow.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.flow.ResponseHandlerResolver;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseHandlerResolverImpl implements ResponseHandlerResolver {

    private final List<ResponseHandler> handlers;
    private final ResponseHandler unsupportedMessageTypeResponseHandler;

    @Override
    public ResponseHandler resolve(Message message) {
        return handlers.stream()
                .filter(h -> h.canHandle(message))
                .findFirst()
                .orElse(unsupportedMessageTypeResponseHandler);
    }
}
