package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeResponse;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SenderService {

    private final RequestHandleService requestHandleService;
    private final ResponseHandleService responseHandleService;
    private final DsProviderHolder dsProviderHolder;

    public Message sendToDs(Message message) {
        log.info("Begin handling requested message: message={}", message.toString());

        Message dsRequestMessage = requestHandleService.handle(message);

        log.info("End handling requested message: message={}", dsRequestMessage.toString());

        if (shouldWeNeedFinishHandling(dsRequestMessage)) {
            return dsRequestMessage;
        }

        Message dsResponseMessage = dsProviderHolder.getDsClient().request(dsRequestMessage);

        log.info("Begin handling responded message: message={}", dsResponseMessage.toString());

        Message sdkResponseMessage = repeatableDsResponseMessageHandle(dsRequestMessage, dsResponseMessage);

        log.info("End handling responded message: message={}", sdkResponseMessage.toString());

        return sdkResponseMessage;
    }

    private Message repeatableDsResponseMessageHandle(Message dsRequestMessage, Message dsResponseMessage) {
        Message sdkResponseMessageCandidate = responseHandleService.handle(dsResponseMessage);
        if (sdkResponseMessageCandidate.isHandleRepetitionNeeded()) {
            log.info("Begin repeatable handling requested message: message={}", dsRequestMessage.toString());

            Message fixedDsRequestMessage = requestHandleService.handle(dsRequestMessage);

            log.info("End repeatable handling requested message: message={}", fixedDsRequestMessage.toString());

            Message fixedDsResponseMessage = dsProviderHolder.getDsClient().request(fixedDsRequestMessage);

            log.info("Begin handling responded message: message={}", fixedDsResponseMessage.toString());

            return repeatableDsResponseMessageHandle(fixedDsRequestMessage, fixedDsResponseMessage);
        }

        return sdkResponseMessageCandidate;
    }

    private boolean shouldWeNeedFinishHandling(Message dsRequestMessage) {
        return dsRequestMessage instanceof Erro
                || dsRequestMessage instanceof PGcs
                || dsRequestMessage instanceof RBKMoneyGetChallengeResponse;
    }
}
