package com.rbkmoney.threeds.server.config.properties;

import lombok.Data;

@Data
public class EnvironmentProperties {

    private String dsUrl;
    private String threeDsRequestorUrl;
    private String threeDsServerUrl;
    private String threeDsServerRefNumber;
    private String threeDsServerOperatorId;
    private int threeDsServerNetworkTimeout;

}
