package com.rbkmoney.threeds.server.service;

public interface LogWrapper {

    void info(String message, String data);

    void warn(String message, Throwable ex);

}
