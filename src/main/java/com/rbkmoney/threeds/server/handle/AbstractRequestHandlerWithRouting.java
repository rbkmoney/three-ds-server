package com.rbkmoney.threeds.server.handle;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.router.DirectoryServerRouter;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRequestHandlerWithRouting extends AbstractRequestHandler {

    private final DirectoryServerProviderHolder providerHolder;
    private final DirectoryServerRouter directoryServerRouter;

    public AbstractRequestHandlerWithRouting(
            DirectoryServerProviderHolder providerHolder,
            DirectoryServerRouter directoryServerRouter,
            Processor<ValidationResult, Message> processor,
            MessageValidatorService validator) {
        super(processor, validator);
        this.providerHolder = providerHolder;
        this.directoryServerRouter = directoryServerRouter;
    }

    @Override
    public Message handle(Message message) {
        DirectoryServerProvider provider = directoryServerRouter.route(message);
        providerHolder.setProvider(provider);

        return super.handle(message);
    }
}
