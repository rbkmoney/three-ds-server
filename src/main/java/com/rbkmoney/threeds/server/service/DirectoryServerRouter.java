package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;

public interface DirectoryServerRouter {

    DirectoryServerProvider route(Message message);
}
