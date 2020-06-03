package com.rbkmoney.threeds.server.holder;

import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;

public interface DirectoryServerProviderHolder {

    DsClient getDsClient();

    EnvironmentProperties getEnvironmentProperties();

    void setProvider(DirectoryServerProvider provider);

    String getTag(Message message);

}
