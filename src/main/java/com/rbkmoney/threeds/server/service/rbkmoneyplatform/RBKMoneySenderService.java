package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeResponse;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationResponse;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.service.AbstractSenderService;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import com.rbkmoney.threeds.server.service.ResponseHandleService;

public class RBKMoneySenderService extends AbstractSenderService {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;
    private final RBKMoneyLogWrapper rbkMoneyLogWrapper;
    private final RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    public RBKMoneySenderService(
            RequestHandleService requestHandleService,
            ResponseHandleService responseHandleService,
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder,
            RBKMoneyLogWrapper rbkMoneyLogWrapper,
            RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService) {
        super(requestHandleService, responseHandleService);
        this.rbkMoneyDsProviderHolder = rbkMoneyDsProviderHolder;
        this.rbkMoneyLogWrapper = rbkMoneyLogWrapper;
        this.rbkMoneyCardRangesStorageService = rbkMoneyCardRangesStorageService;
    }

    @Override
    protected void log(String message, Message data) {
        rbkMoneyLogWrapper.info(message, data);
    }

    @Override
    protected Message request(Message dsRequestMessage) {
        return rbkMoneyDsProviderHolder.getDsClient().request(dsRequestMessage);
    }

    @Override
    protected Message handleResponse(Message dsRequestMessage, Message dsResponseMessage) {
        Message sdkResponseMessage = responseHandleService.handle(dsResponseMessage);
        if (sdkResponseMessage instanceof RBKMoneyPreparationResponse
                && dsResponseMessage instanceof PRes) {
            RBKMoneyPreparationResponse rbkMoneyPreparationResponse = (RBKMoneyPreparationResponse) sdkResponseMessage;
            PRes pRes = (PRes) dsResponseMessage;

            rbkMoneyCardRangesStorageService.updateCardRanges(rbkMoneyPreparationResponse.getProviderId(), pRes);
        }
        return sdkResponseMessage;
    }

    @Override
    protected boolean shouldWeNeedFinishHandling(Message dsRequestMessage) {
        return dsRequestMessage instanceof Erro
                || dsRequestMessage instanceof RBKMoneyGetChallengeResponse;
    }
}
