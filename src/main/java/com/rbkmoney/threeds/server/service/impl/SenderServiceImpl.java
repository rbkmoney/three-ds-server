package com.rbkmoney.threeds.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import com.rbkmoney.threeds.server.service.ResponseHandleService;
import com.rbkmoney.threeds.server.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderServiceImpl implements SenderService {

    private final RequestHandleService requestHandleService;
    private final ResponseHandleService responseHandleService;
    private final DsClient dsClient;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Message sendToDs(Message message) {
        log.info("\n" + objectMapper.writeValueAsString(message));

        Message dsRequestMessage = requestHandleService.handle(message);

        log.info("\n" + objectMapper.writeValueAsString(dsRequestMessage));

        if (dsRequestMessage instanceof Erro) {
            return dsRequestMessage;
        }

        Message dsResponseMessage = dsClient.request(dsRequestMessage);

        log.info("\n" + objectMapper.writeValueAsString(dsResponseMessage));

        Message sdkResponseMessage = repeatableDsResponseMessageHandle(dsRequestMessage, dsResponseMessage);

        log.info("\n" + objectMapper.writeValueAsString(sdkResponseMessage));

        return sdkResponseMessage;
    }

    @SneakyThrows
    private Message repeatableDsResponseMessageHandle(Message dsRequestMessage, Message dsResponseMessage) {
        Message sdkResponseMessageCandidate = responseHandleService.handle(dsResponseMessage);
        if (sdkResponseMessageCandidate.isHandleRepetitionNeeded()) {
            log.info("\n" + objectMapper.writeValueAsString(dsRequestMessage));

            Message fixedDsRequestMessage = requestHandleService.handle(dsRequestMessage);

            log.info("\n" + objectMapper.writeValueAsString(fixedDsRequestMessage));

            Message fixedDsResponseMessage = dsClient.request(fixedDsRequestMessage);

            log.info("\n" + objectMapper.writeValueAsString(fixedDsResponseMessage));

            return repeatableDsResponseMessageHandle(fixedDsRequestMessage, fixedDsResponseMessage);
        }

        return sdkResponseMessageCandidate;
    }
}
