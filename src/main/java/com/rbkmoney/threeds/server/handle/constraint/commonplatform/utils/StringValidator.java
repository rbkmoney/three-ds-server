package com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils;

import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;

public interface StringValidator {

    boolean isNotNull(String candidate);

    ConstraintValidationResult validateStringOnlyBlank(String fieldName, String candidate);

    ConstraintValidationResult validateStringWithConstLength(String fieldName, Integer length, String candidate);

    ConstraintValidationResult validateStringWithMaxLength(String fieldName, Integer maxLength, String candidate);

    ConstraintValidationResult validateStringWithMinAndMaxLength(String fieldName, Integer maxLength, Integer minLength, String candidate);

    ConstraintValidationResult validateUUID(String fieldName, String candidate);

}
