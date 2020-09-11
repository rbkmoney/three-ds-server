package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;

public interface LogWrapper {

    void info(String message, Message data);

    void warn(String message, Throwable ex);

}
