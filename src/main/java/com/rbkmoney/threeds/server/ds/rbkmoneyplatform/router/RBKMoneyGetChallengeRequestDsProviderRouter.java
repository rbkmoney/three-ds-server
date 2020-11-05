package com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.DsProviderRouter;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestDsProviderRouter implements DsProviderRouter {

    private final ChallengeFlowTransactionInfoStorageService transactionInfoStorageService;

    @Override
    public DsProvider route(Message message) {
        RBKMoneyGetChallengeRequest request = (RBKMoneyGetChallengeRequest) message;
        ChallengeFlowTransactionInfo challengeFlowTransactionInfo = transactionInfoStorageService.getChallengeFlowTransactionInfo(request.getThreeDSServerTransID());
        return DsProvider.of(challengeFlowTransactionInfo.getDsProviderId());
    }
}
