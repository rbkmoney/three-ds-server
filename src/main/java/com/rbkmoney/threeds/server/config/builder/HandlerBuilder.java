package com.rbkmoney.threeds.server.config.builder;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.ds.DsProviderRouter;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.handle.*;
import com.rbkmoney.threeds.server.processor.Processor;
import com.rbkmoney.threeds.server.service.MessageValidatorService;

import java.util.function.Predicate;

public class HandlerBuilder {

    public static RequestHandler createRequestHandler(
            Processor<ValidationResult, Message> processor,
            MessageValidatorService messageValidatorService,
            Predicate<Message> messagePredicate) {
        return new AbstractRequestHandler(processor, messageValidatorService) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }

    public static RequestHandler createRequestHandlerWithRouting(
            DsProviderHolder dsProviderHolder,
            DsProviderRouter dsProviderRouter,
            Processor<ValidationResult, Message> processor,
            MessageValidatorService messageValidatorService,
            Predicate<Message> messagePredicate) {
        return new AbstractRequestHandlerWithRouting(dsProviderHolder, dsProviderRouter, processor, messageValidatorService) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }

    public static DsRequestHandler createDsRequestHandler(
            Processor<ValidationResult, Message> processor,
            MessageValidatorService messageValidatorService,
            Predicate<Message> messagePredicate) {
        return new AbstractRequestHandler(processor, messageValidatorService) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }

    public static ResponseHandler createResponseHandler(
            Processor<ValidationResult, Message> processor,
            MessageValidatorService messageValidatorService,
            DsProviderHolder dsProviderHolder,
            Predicate<Message> messagePredicate) {
        return new AbstractResponseHandler(processor, messageValidatorService, dsProviderHolder) {

            @Override
            public boolean canHandle(Message message) {
                return messagePredicate.test(message);
            }
        };
    }
}
