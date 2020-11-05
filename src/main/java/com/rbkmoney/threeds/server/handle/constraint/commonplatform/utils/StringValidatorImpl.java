package com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils;

import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class StringValidatorImpl implements StringValidator {

    private final Pattern uuidPattern = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isNotNull(String candidate) {
        return candidate != null;
    }

    @Override
    public ConstraintValidationResult validateStringOnlyBlank(String fieldName, String candidate) {
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(NOT_BLANK, fieldName);
        }

        return ConstraintValidationResult.success();
    }

    @Override
    public ConstraintValidationResult validateStringWithConstLength(String fieldName, Integer length, String candidate) {
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(NOT_BLANK, fieldName);
        }

        if (candidate.length() != length) {
            return ConstraintValidationResult.failure(PATTERN, fieldName);
        }

        return ConstraintValidationResult.success();
    }

    @Override
    public ConstraintValidationResult validateStringWithMaxLength(String fieldName, Integer maxLength, String candidate) {
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(NOT_BLANK, fieldName);
        }

        if (candidate.length() > maxLength) {
            return ConstraintValidationResult.failure(PATTERN, fieldName);
        }

        return ConstraintValidationResult.success();
    }

    @Override
    public ConstraintValidationResult validateStringWithMinAndMaxLength(String fieldName, Integer maxLength, Integer minLength, String candidate) {
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(NOT_BLANK, fieldName);
        }

        if (candidate.length() > maxLength || candidate.length() < minLength) {
            return ConstraintValidationResult.failure(PATTERN, fieldName);
        }

        return ConstraintValidationResult.success();
    }

    @Override
    public ConstraintValidationResult validateUUID(String fieldName, String candidate) {
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(NOT_BLANK, fieldName);
        }

        if (!uuidPattern.matcher(candidate).matches()) {
            return ConstraintValidationResult.failure(PATTERN, fieldName);
        }

        return ConstraintValidationResult.success();
    }
}
