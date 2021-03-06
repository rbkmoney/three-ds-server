package com.rbkmoney.threeds.server.config.testplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.service.LogWrapper;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformLogWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class LogWrapperConfig {

    @Bean
    public LogWrapper logWrapper(TestPlatformLogWrapper testPlatformLogWrapper) {
        return testPlatformLogWrapper;
    }

    @Bean
    public TestPlatformLogWrapper testPlatformLogWrapper(ObjectMapper objectMapper) {
        return new TestPlatformLogWrapper(objectMapper);
    }
}
