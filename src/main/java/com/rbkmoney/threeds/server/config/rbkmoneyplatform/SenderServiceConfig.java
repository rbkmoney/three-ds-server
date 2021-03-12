package com.rbkmoney.threeds.server.config.rbkmoneyplatform;

import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import com.rbkmoney.threeds.server.service.ResponseHandleService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyLogWrapper;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneySenderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
public class SenderServiceConfig {

    @Bean
    public RBKMoneySenderService rbkMoneySenderService(
            RequestHandleService requestHandleService,
            ResponseHandleService responseHandleService,
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder,
            RBKMoneyLogWrapper rbkMoneyLogWrapper,
            RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService) {
        return new RBKMoneySenderService(requestHandleService, responseHandleService, rbkMoneyDsProviderHolder,
                rbkMoneyLogWrapper, rbkMoneyCardRangesStorageService);
    }
}
