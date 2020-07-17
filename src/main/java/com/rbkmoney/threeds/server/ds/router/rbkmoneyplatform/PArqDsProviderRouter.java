package com.rbkmoney.threeds.server.ds.router.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.router.DsProviderRouter;
import com.rbkmoney.threeds.server.exeption.DirectoryServerRoutingException;
import com.rbkmoney.threeds.server.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class PArqDsProviderRouter implements DsProviderRouter {

    private final CacheService cacheService;

    @Override
    public DsProvider route(Message message) {
        PArq pArq = (PArq) message;

        String acctNumber = pArq.getAcctNumber();

        return stream(DsProvider.values())
                .filter(provider -> cacheService.isInCardRange(provider.getId(), acctNumber))
                .findFirst()
                .orElseThrow(() -> new DirectoryServerRoutingException("Unable to route pArq message with id=" + pArq.getThreeDSServerTransID()));
    }
}
