package com.rbkmoney.threeds.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("client.ssl")
@Data
public class KeystoreProperties {

    private String trustStore;
    private String trustStorePassword;

}
