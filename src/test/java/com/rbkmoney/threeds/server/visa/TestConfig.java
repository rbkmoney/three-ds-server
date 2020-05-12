package com.rbkmoney.threeds.server.visa;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

@TestConfiguration
public class TestConfig {

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();

        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put(WebApplicationContext.SCOPE_REQUEST,
                new SimpleThreadScope());
        scopes.put(WebApplicationContext.SCOPE_SESSION,
                new SimpleThreadScope());
        scopeConfigurer.setScopes(scopes);

        return scopeConfigurer;
    }
}