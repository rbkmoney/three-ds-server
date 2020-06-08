package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.handle.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestHandlerResolver {

    private final List<RequestHandler> handlers;
    private final RequestHandler unsupportedMessageTypeRequestHandler;

    public RequestHandler resolve(Message message) {
        return handlers.stream()
                .filter(h -> h.canHandle(message))
                .findFirst()
                .orElse(unsupportedMessageTypeRequestHandler);
    }
}
