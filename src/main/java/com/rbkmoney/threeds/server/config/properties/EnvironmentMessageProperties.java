package com.rbkmoney.threeds.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("environment.message")
public class EnvironmentMessageProperties {

    private String messageVersion;
    private List<String> validMessageVersions;
    private String pMessageVersion;
}
