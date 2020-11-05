package com.rbkmoney.threeds.server.ds;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;

public interface DsClient {

    Message request(Message message);

    void notifyDsAboutError(Erro message);

}
