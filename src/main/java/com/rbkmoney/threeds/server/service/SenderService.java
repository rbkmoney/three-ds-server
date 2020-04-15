package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;

public interface SenderService {

    Message sendToDs(Message message);
}
