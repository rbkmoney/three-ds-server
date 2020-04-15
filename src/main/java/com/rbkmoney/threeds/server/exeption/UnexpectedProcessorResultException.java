package com.rbkmoney.threeds.server.exeption;

public class UnexpectedProcessorResultException extends RuntimeException {

    public UnexpectedProcessorResultException() {
        super("Unexpected ended processor handling");
    }

    public UnexpectedProcessorResultException(String message) {
        super(message);
    }

    public UnexpectedProcessorResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedProcessorResultException(Throwable cause) {
        super(cause);
    }

    public UnexpectedProcessorResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
