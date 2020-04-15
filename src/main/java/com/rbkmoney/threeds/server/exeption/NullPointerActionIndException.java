package com.rbkmoney.threeds.server.exeption;

public class NullPointerActionIndException extends RuntimeException {

    public NullPointerActionIndException(String message) {
        super(message);
    }

    public NullPointerActionIndException(Throwable cause) {
        super(cause);
    }

    public NullPointerActionIndException(String message, Throwable cause) {
        super(message, cause);
    }
}
