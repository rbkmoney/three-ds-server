package com.rbkmoney.threeds.server.ds;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;

import java.util.Optional;

public interface DsProviderHolder {

    DsClient getDsClient();

    EnvironmentProperties getEnvironmentProperties();

    Optional<String> getTag(Message message);

    void setDsProvider(DsProvider dsProvider);

    // todo rm (requestScope)
    Optional<String> getDsProvider();

}
