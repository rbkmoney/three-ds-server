package com.rbkmoney.threeds.server.ds.router.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.router.DsProviderRouter;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestDsProviderRouter implements DsProviderRouter {

    private final CacheService cacheService;

    @Override
    public DsProvider route(Message message) {
        RBKMoneyGetChallengeRequest request = (RBKMoneyGetChallengeRequest) message;
        ChallengeFlowTransactionInfo challengeFlowTransactionInfo = cacheService.getChallengeFlowTransactionInfo(request.getThreeDSServerTransID());
        return DsProvider.of(challengeFlowTransactionInfo.getDsProviderId());
    }
}
