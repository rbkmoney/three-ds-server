package com.rbkmoney.threeds.server.processor;

public interface Processor<T, R> {

    R process(T object);

}
