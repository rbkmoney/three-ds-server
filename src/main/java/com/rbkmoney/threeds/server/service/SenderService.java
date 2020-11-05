package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeResponse;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
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
    private final LogWrapper logWrapper;

    public Message sendToDs(Message message) {
        logWrapper.info("Before requestHandle: {}", message);

        Message dsRequestMessage = requestHandleService.handle(message);

        logWrapper.info("After requestHandle", dsRequestMessage);

        if (shouldWeNeedFinishHandling(dsRequestMessage)) {
            return dsRequestMessage;
        }

        Message dsResponseMessage = dsProviderHolder.getDsClient().request(dsRequestMessage);

        logWrapper.info("Before responseHandle", dsResponseMessage);

        Message sdkResponseMessage = repeatableDsResponseMessageHandle(dsRequestMessage, dsResponseMessage);

        logWrapper.info("After responseHandle", sdkResponseMessage);

        return sdkResponseMessage;
    }

    private Message repeatableDsResponseMessageHandle(Message dsRequestMessage, Message dsResponseMessage) {
        Message sdkResponseMessageCandidate = responseHandleService.handle(dsResponseMessage);
        if (sdkResponseMessageCandidate.isHandleRepetitionNeeded()) {
            logWrapper.info("[repeat with fixed message] Before requestHandle", dsRequestMessage);

            Message fixedDsRequestMessage = requestHandleService.handle(dsRequestMessage);

            logWrapper.info("[repeat with fixed message] After requestHandle", fixedDsRequestMessage);

            Message fixedDsResponseMessage = dsProviderHolder.getDsClient().request(fixedDsRequestMessage);

            logWrapper.info("[repeat with fixed message] Before responseHandle", fixedDsResponseMessage);

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
