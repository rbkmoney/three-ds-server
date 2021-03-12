package com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.RBKMoneyDsProviderRouter;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestDsProviderRouter implements RBKMoneyDsProviderRouter {

    private final RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService;

    @Override
    public DsProvider route(Message message) {
        RBKMoneyGetChallengeRequest request = (RBKMoneyGetChallengeRequest) message;
        ChallengeFlowTransactionInfo challengeFlowTransactionInfo = rbkMoneyChallengeFlowTransactionInfoStorageService
                .getChallengeFlowTransactionInfo(request.getThreeDSServerTransID());
        return DsProvider.of(challengeFlowTransactionInfo.getDsProviderId());
    }
}
