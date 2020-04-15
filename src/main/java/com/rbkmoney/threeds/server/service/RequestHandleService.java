package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;

public interface RequestHandleService {

    Message handle(Message message);

}
