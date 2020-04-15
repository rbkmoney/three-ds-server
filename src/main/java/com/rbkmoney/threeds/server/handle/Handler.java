package com.rbkmoney.threeds.server.handle;

public interface Handler<T, R> {

    boolean canHandle(T o);

    R handle(T o);
}
