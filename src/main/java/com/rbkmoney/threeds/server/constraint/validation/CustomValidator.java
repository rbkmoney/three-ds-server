package com.rbkmoney.threeds.server.constraint.validation;

import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.ValidatorContextEnricherService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class CustomValidator<T> implements ConstraintValidator<CustomValidation, T> {

    private final List<ConstraintValidationHandler<T>> handlers;
    private final ValidatorContextEnricherService contextEnricher;

    @Override
    public boolean isValid(T message, ConstraintValidatorContext ctx) {
        ConstraintValidationResult constraintValidationResult = handlers.stream()
                .filter(handler -> handler.isValidMessageVersion(message))
                .filter(handler -> handler.canHandle(message))
                .map(handler -> handler.handle(message))
                .filter(not(ConstraintValidationResult::isValid))
                .findAny()
                .orElse(ConstraintValidationResult.success());

        if (!constraintValidationResult.isValid()) {
            contextEnricher.enrich(ctx, constraintValidationResult.getConstraintType(), constraintValidationResult.getFieldName());
        }

        return constraintValidationResult.isValid();
    }
}
