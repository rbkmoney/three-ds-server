package com.rbkmoney.threeds.server.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EnvironmentProperties {

    @Length(max = 2048)
    private String dsUrl;
    @Length(max = 2048)
    private String threeDsServerUrl;
    @Length(max = 32)
    private String threeDsServerRefNumber;
    private int threeDsServerNetworkTimeout;
}
