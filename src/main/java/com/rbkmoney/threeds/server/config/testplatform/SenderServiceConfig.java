package com.rbkmoney.threeds.server.config.testplatform;

import com.rbkmoney.threeds.server.ds.testplatform.TestPlatformDsClient;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import com.rbkmoney.threeds.server.service.ResponseHandleService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformLogWrapper;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSenderService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
public class SenderServiceConfig {

    @Bean
    public TestPlatformSenderService testPlatformSenderService(
            RequestHandleService requestHandleService,
            ResponseHandleService responseHandleService,
            TestPlatformLogWrapper testPlatformLogWrapper,
            TestPlatformDsClient testPlatformDsClient,
            TestPlatformSerialNumStorageService testPlatformSerialNumStorageService,
            TestPlatformCardRangesStorageService testPlatformCardRangesStorageService) {
        return new TestPlatformSenderService(requestHandleService, responseHandleService, testPlatformLogWrapper, testPlatformDsClient, testPlatformSerialNumStorageService, testPlatformCardRangesStorageService);
    }
}
