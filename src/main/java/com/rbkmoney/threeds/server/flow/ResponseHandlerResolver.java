package com.rbkmoney.threeds.server.flow;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.handle.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseHandlerResolver {

    private final List<ResponseHandler> handlers;
    private final ResponseHandler unsupportedMessageTypeResponseHandler;

    public ResponseHandler resolve(Message message) {
        return handlers.stream()
                .filter(h -> h.canHandle(message))
                .findFirst()
                .orElse(unsupportedMessageTypeResponseHandler);
    }
}
