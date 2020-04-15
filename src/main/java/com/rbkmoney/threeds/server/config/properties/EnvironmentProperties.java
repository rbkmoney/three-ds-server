package com.rbkmoney.threeds.server.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("environment")
@Data
public class EnvironmentProperties {

    @Length(max = 2048)
    private String dsUrl;
    @Length(max = 2048)
    private String threeDsServerUrl;
    @Length(max = 32)
    private String threeDsServerRefNumber;
    private int threeDsServerNetworkTimeout;
    private String messageVersion;
    private String pMessageVersion;
    private List<String> validMessageVersions;

}
