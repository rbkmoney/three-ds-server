package com.rbkmoney.threeds.server.handle;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.RBKMoneyDsProviderRouter;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;

public abstract class AbstractRequestHandlerWithRouting extends AbstractRequestHandler {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;
    private final RBKMoneyDsProviderRouter rbkMoneyDsProviderRouter;

    public AbstractRequestHandlerWithRouting(
            RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder,
            RBKMoneyDsProviderRouter rbkMoneyDsProviderRouter,
            Processor<ValidationResult, Message> processor,
            MessageValidatorService messageValidatorService) {
        super(processor, messageValidatorService);
        this.rbkMoneyDsProviderHolder = rbkMoneyDsProviderHolder;
        this.rbkMoneyDsProviderRouter = rbkMoneyDsProviderRouter;
    }

    @Override
    public Message handle(Message message) {
        DsProvider dsProvider = rbkMoneyDsProviderRouter.route(message);
        rbkMoneyDsProviderHolder.setDsProvider(dsProvider);

        return super.handle(message);
    }
}
