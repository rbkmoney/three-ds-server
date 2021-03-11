package com.rbkmoney.threeds.server.dto;

import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.root.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResult {

    private boolean isValid;

    private ErrorCode errorCode;

    private String errorDetail;

    private String errorDescription;

    private Message message;

    private ValidationResult() {
    }

    public static ValidationResult success(Message message) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(true);
        validationResult.setMessage(message);
        return validationResult;
    }

    public static ValidationResult failure(ErrorCode errorCode, String errorDetail, String errorDescription,
                                           Message message) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(false);
        validationResult.setErrorCode(errorCode);
        validationResult.setErrorDetail(errorDetail);
        validationResult.setErrorDescription(errorDescription);
        validationResult.setMessage(message);
        return validationResult;
    }
}
