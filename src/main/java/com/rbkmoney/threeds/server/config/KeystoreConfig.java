package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeystoreConfig {

    @Bean
    @ConfigurationProperties("client.ssl.visa")
    public KeystoreProperties visaKeystoreProperties() {
        return new KeystoreProperties();
    }

    @Bean
    @ConfigurationProperties("client.ssl.mastercard")
    public KeystoreProperties mastercardKeystoreProperties() {
        return new KeystoreProperties();
    }

    @Bean
    @ConfigurationProperties("client.ssl.mir")
    public KeystoreProperties mirKeystoreProperties() {
        return new KeystoreProperties();
    }

    @Bean
    @ConfigurationProperties("client.ssl.test")
    public KeystoreProperties testKeystoreProperties() {
        return new KeystoreProperties();
    }
}
