package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.handle.DsRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DsRequestHandlerResolver {

    private final List<DsRequestHandler> handlers;
    private final DsRequestHandler unsupportedMessageTypeDsRequestHandler;

    public DsRequestHandler resolve(Message message) {
        return handlers.stream()
                .filter(h -> h.canHandle(message))
                .findFirst()
                .orElse(unsupportedMessageTypeDsRequestHandler);
    }
}
