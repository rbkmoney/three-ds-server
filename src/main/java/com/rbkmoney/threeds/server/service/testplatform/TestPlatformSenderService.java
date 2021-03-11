package com.rbkmoney.threeds.server.service.testplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrs;
import com.rbkmoney.threeds.server.ds.testplatform.TestPlatformDsClient;
import com.rbkmoney.threeds.server.service.AbstractSenderService;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import com.rbkmoney.threeds.server.service.ResponseHandleService;

@SuppressWarnings({"checkstyle:localvariablename"})
public class TestPlatformSenderService extends AbstractSenderService {

    private final TestPlatformLogWrapper testPlatformLogWrapper;
    private final TestPlatformDsClient testPlatformDsClient;
    private final TestPlatformSerialNumStorageService testPlatformSerialNumStorageService;
    private final TestPlatformCardRangesStorageService testPlatformCardRangesStorageService;

    public TestPlatformSenderService(
            RequestHandleService requestHandleService,
            ResponseHandleService responseHandleService,
            TestPlatformLogWrapper testPlatformLogWrapper,
            TestPlatformDsClient testPlatformDsClient,
            TestPlatformSerialNumStorageService testPlatformSerialNumStorageService,
            TestPlatformCardRangesStorageService testPlatformCardRangesStorageService) {
        super(requestHandleService, responseHandleService);
        this.testPlatformLogWrapper = testPlatformLogWrapper;
        this.testPlatformDsClient = testPlatformDsClient;
        this.testPlatformSerialNumStorageService = testPlatformSerialNumStorageService;
        this.testPlatformCardRangesStorageService = testPlatformCardRangesStorageService;
    }

    @Override
    protected void log(String message, Message data) {
        testPlatformLogWrapper.info(message, data);
    }

    @Override
    protected Message request(Message dsRequestMessage) {
        return testPlatformDsClient.request(dsRequestMessage);
    }

    @Override
    protected Message handleResponse(Message dsRequestMessage, Message dsResponseMessage) {
        Message sdkResponseMessage = repeatableDsResponseMessageHandle(dsRequestMessage, dsResponseMessage);
        if (sdkResponseMessage instanceof PPrs
                && dsResponseMessage instanceof PRes
                && ((PRes) dsResponseMessage).getSerialNum() != null) {
            PRes pRes = (PRes) dsResponseMessage;

            testPlatformSerialNumStorageService.saveSerialNum(pRes.getUlTestCaseId(), pRes.getSerialNum());
            testPlatformCardRangesStorageService.updateCardRanges(pRes);
        }
        return sdkResponseMessage;
    }

    private Message repeatableDsResponseMessageHandle(Message dsRequestMessage, Message dsResponseMessage) {
        Message sdkResponseMessageCandidate = responseHandleService.handle(dsResponseMessage);
        if (sdkResponseMessageCandidate instanceof Erro
                && dsResponseMessage.isHandleRepetitionNeeded()) {
            log("[repeat] Before requestHandle", dsRequestMessage);

            Message fixedDsRequestMessage = requestHandleService.handle(dsRequestMessage);

            log("[repeat] After requestHandle", fixedDsRequestMessage);

            Message fixedDsResponseMessage = request(fixedDsRequestMessage);

            log("[repeat] Before responseHandle", fixedDsResponseMessage);

            return repeatableDsResponseMessageHandle(fixedDsRequestMessage, fixedDsResponseMessage);
        }

        return sdkResponseMessageCandidate;
    }

    @Override
    protected boolean shouldWeNeedFinishHandling(Message dsRequestMessage) {
        return dsRequestMessage instanceof Erro
                || dsRequestMessage instanceof PGcs;
    }
}
