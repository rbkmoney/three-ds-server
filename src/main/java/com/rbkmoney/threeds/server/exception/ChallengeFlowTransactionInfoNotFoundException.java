package com.rbkmoney.threeds.server.exception;

public class ChallengeFlowTransactionInfoNotFoundException extends RuntimeException {

    public ChallengeFlowTransactionInfoNotFoundException() {
    }

    public ChallengeFlowTransactionInfoNotFoundException(String message) {
        super(message);
    }

    public ChallengeFlowTransactionInfoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChallengeFlowTransactionInfoNotFoundException(Throwable cause) {
        super(cause);
    }

    public ChallengeFlowTransactionInfoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
