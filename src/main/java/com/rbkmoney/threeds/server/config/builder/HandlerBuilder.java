package com.rbkmoney.threeds.server.config.builder;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.*;
import com.rbkmoney.threeds.server.holder.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.router.DirectoryServerRouter;
import com.rbkmoney.threeds.server.service.MessageValidatorService;

import java.util.function.Predicate;

public class HandlerBuilder {

    public static RequestHandler createRequestHandler(
            Processor<ValidationResult, Message> processorChain,
            MessageValidatorService validator,
            Predicate<Message> messagePredicate) {
        return new AbstractRequestHandler(processorChain, validator) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }

    public static RequestHandler createRequestHandlerWithRouting(
            DirectoryServerProviderHolder providerHolder,
            DirectoryServerRouter directoryServerRouter,
            Processor<ValidationResult, Message> processor,
            MessageValidatorService validator,
            Predicate<Message> messagePredicate) {
        return new AbstractRequestHandlerWithRouting(providerHolder, directoryServerRouter, processor, validator) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }

    public static DsRequestHandler createDsRequestHandler(
            Processor<ValidationResult, Message> processorChain,
            MessageValidatorService validator,
            Predicate<Message> messagePredicate) {
        return new AbstractRequestHandler(processorChain, validator) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }

    public static ResponseHandler createResponseHandler(
            Processor<ValidationResult, Message> processor,
            MessageValidatorService validator,
            DirectoryServerProviderHolder providerHolder,
            Predicate<Message> messagePredicate) {
        return new AbstractResponseHandler(processor, validator, providerHolder) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }
}
