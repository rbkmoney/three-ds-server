package com.rbkmoney.threeds.server.handle.impl;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.router.DirectoryServerRouter;
import com.rbkmoney.threeds.server.service.MessageValidatorService;

public class PArqToAReqHandlerImpl extends RequestHandlerImpl {

    private final DirectoryServerProviderHolder providerHolder;
    private final DirectoryServerRouter directoryServerRouter;

    public PArqToAReqHandlerImpl(
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

    @Override
    public boolean canHandle(Message message) {
        return message instanceof PArq;
    }
}
