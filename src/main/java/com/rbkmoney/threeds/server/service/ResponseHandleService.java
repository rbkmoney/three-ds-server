package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;

public interface ResponseHandleService {

    Message handle(Message message);

}
