package com.rbkmoney.threeds.server.ds.rbkmoneyplatform.router;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.DsProviderRouter;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestDsProviderRouter implements DsProviderRouter {

    private final CardRangesStorageService cardRangesStorageService;

    @Override
    public DsProvider route(Message message) {
        RBKMoneyAuthenticationRequest request = (RBKMoneyAuthenticationRequest) message;

        String acctNumber = request.getAcctNumber();

        // todo isInCardRange(acctNumber)
        return stream(DsProvider.values())
                .filter(provider -> cardRangesStorageService.isInCardRange(provider.getId(), acctNumber))
                .findFirst()
                .orElse(null);
    }
}