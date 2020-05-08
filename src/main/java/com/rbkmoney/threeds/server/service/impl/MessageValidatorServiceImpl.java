package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import com.rbkmoney.threeds.server.service.MessageValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageValidatorServiceImpl implements MessageValidatorService {

    private final Validator validator;
    private final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

    @Override
    public ValidationResult validate(Message message) {
        Set<ConstraintViolation<Message>> errors = validator.validate(message);
        return errors.stream()
                .findAny()
                .map(error -> createFailureValidationResult(error, message))
                .orElse(ValidationResult.success(message));
    }

    private ValidationResult createFailureValidationResult(ConstraintViolation<Message> error, Message message) {
        return ValidationResult.failure(
                errorCodeResolver.resolve(error.getMessageTemplate()),
                getErrorDetail(error),
                errorMessageResolver.resolveErrorDescription(error.getMessageTemplate()),
                message
        );
    }

    private String getErrorDetail(ConstraintViolation<Message> error) {
        return error.getPropertyPath().toString();
    }
}
