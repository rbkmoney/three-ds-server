package com.rbkmoney.threeds.server.exeption;

public class UnsupportedMethodException extends RuntimeException {

    public UnsupportedMethodException() {
        super("Unsupported method");
    }

    public UnsupportedMethodException(String message) {
        super(message);
    }

    public UnsupportedMethodException(Throwable cause) {
        super(cause);
    }

    public UnsupportedMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
