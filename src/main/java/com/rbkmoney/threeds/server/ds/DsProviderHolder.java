package com.rbkmoney.threeds.server.ds;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;

public interface DsProviderHolder {

    DsClient getDsClient();

    EnvironmentProperties getEnvironmentProperties();

}
