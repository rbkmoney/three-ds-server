package com.rbkmoney.threeds.server.ds.holder;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.ds.client.DsClient;

import java.util.Optional;

public interface DsProviderHolder {

    DsClient getDsClient();

    EnvironmentProperties getEnvironmentProperties();

    void setDsProvider(DsProvider provider);

    Optional<String> getTag(Message message);

}
