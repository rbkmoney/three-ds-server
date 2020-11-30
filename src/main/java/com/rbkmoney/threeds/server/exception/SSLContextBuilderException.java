package com.rbkmoney.threeds.server.exception;

public class SSLContextBuilderException extends RuntimeException {

    public SSLContextBuilderException() {
    }

    public SSLContextBuilderException(String message) {
        super(message);
    }

    public SSLContextBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSLContextBuilderException(Throwable cause) {
        super(cause);
    }

    public SSLContextBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
