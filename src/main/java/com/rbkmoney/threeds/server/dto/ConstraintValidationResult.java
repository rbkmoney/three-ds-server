package com.rbkmoney.threeds.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConstraintValidationResult {

    private boolean isValid;

    private ConstraintType constraintType;

    private String fieldName;

    private ConstraintValidationResult() {
    }

    public static ConstraintValidationResult success() {
        ConstraintValidationResult constraintValidationResult = new ConstraintValidationResult();
        constraintValidationResult.setValid(true);
        return constraintValidationResult;
    }

    public static ConstraintValidationResult failure(ConstraintType constraintType, String fieldName) {
        ConstraintValidationResult constraintValidationResult = new ConstraintValidationResult();
        constraintValidationResult.setValid(false);
        constraintValidationResult.setConstraintType(constraintType);
        constraintValidationResult.setFieldName(fieldName);
        return constraintValidationResult;
    }
}
