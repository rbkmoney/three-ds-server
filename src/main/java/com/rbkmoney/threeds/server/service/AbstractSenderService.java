package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractSenderService implements SenderService {

    protected final RequestHandleService requestHandleService;
    protected final ResponseHandleService responseHandleService;

    public Message sendToDs(Message message) {
        log("Before requestHandle: {}", message);

        Message dsRequestMessage = requestHandleService.handle(message);

        log("After requestHandle", dsRequestMessage);

        if (shouldWeNeedFinishHandling(dsRequestMessage)) {
            return dsRequestMessage;
        }

        Message dsResponseMessage = request(dsRequestMessage);

        log("Before responseHandle", dsResponseMessage);

        Message sdkResponseMessage = handleResponse(dsRequestMessage, dsResponseMessage);

        log("After responseHandle", sdkResponseMessage);

        return sdkResponseMessage;
    }

    protected abstract void log(String message, Message data);

    protected abstract Message request(Message dsRequestMessage);

    protected abstract Message handleResponse(Message dsRequestMessage, Message dsResponseMessage);

    protected abstract boolean shouldWeNeedFinishHandling(Message dsRequestMessage);

}
