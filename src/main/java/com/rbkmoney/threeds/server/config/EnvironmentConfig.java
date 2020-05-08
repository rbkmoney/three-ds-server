package com.rbkmoney.threeds.server.config;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfig {

    @Bean
    @ConfigurationProperties("environment.visa")
    public EnvironmentProperties visaEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("environment.mastercard")
    public EnvironmentProperties mastercardEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("environment.mir")
    public EnvironmentProperties mirEnvironmentProperties() {
        return new EnvironmentProperties();
    }

    @Bean
    @ConfigurationProperties("environment.test")
    public EnvironmentProperties testEnvironmentProperties() {
        return new EnvironmentProperties();
    }
}
