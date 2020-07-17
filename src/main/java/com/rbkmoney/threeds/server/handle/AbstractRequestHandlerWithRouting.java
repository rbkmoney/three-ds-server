package com.rbkmoney.threeds.server.handle;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.holder.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.router.DsProviderRouter;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRequestHandlerWithRouting extends AbstractRequestHandler {

    private final DsProviderHolder dsProviderHolder;
    private final DsProviderRouter dsProviderRouter;

    public AbstractRequestHandlerWithRouting(
            DsProviderHolder dsProviderHolder,
            DsProviderRouter dsProviderRouter,
            Processor<ValidationResult, Message> processor,
            MessageValidatorService messageValidatorService) {
        super(processor, messageValidatorService);
        this.dsProviderHolder = dsProviderHolder;
        this.dsProviderRouter = dsProviderRouter;
    }

    @Override
    public Message handle(Message message) {
        DsProvider dsProvider = dsProviderRouter.route(message);
        dsProviderHolder.setDsProvider(dsProvider);

        return super.handle(message);
    }
}
