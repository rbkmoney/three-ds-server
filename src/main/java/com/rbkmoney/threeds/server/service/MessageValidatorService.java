package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static com.rbkmoney.threeds.server.dto.ValidationResult.success;

@Service
@RequiredArgsConstructor
public class MessageValidatorService {

    private final Validator validator;
    private final ErrorCodeResolver errorCodeResolver;
    private final ErrorMessageResolver errorMessageResolver;

    public ValidationResult validate(Message message) {
        Set<ConstraintViolation<Message>> errors = validator.validate(message);
        return errors.stream()
                .findAny()
                .map(error -> failure(error, message))
                .orElse(success(message));
    }

    private ValidationResult failure(ConstraintViolation<Message> error, Message message) {
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
