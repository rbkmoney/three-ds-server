package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.service.ChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformChallengeFlowTransactionInfoStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class StorageServiceConfig {

    @Bean
    public ChallengeFlowTransactionInfoStorageService challengeFlowTransactionInfoStorageService(
            TestPlatformChallengeFlowTransactionInfoStorageService testPlatformChallengeFlowTransactionInfoStorageService) {
        return testPlatformChallengeFlowTransactionInfoStorageService;
    }

    @Bean
    public TestPlatformChallengeFlowTransactionInfoStorageService testPlatformChallengeFlowTransactionInfoStorageService() {
        return new TestPlatformChallengeFlowTransactionInfoStorageService();
    }

    @Bean
    public TestPlatformCardRangesStorageService testPlatformCardRangesStorageService() {
        return new TestPlatformCardRangesStorageService();
    }

    @Bean
    public TestPlatformSerialNumStorageService testPlatformSerialNumStorageService() {
        return new TestPlatformSerialNumStorageService();
    }
}
