package com.rbkmoney.threeds.server.service.testplatform;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class TestPlatformSerialNumStorageService {

    private final Cache<String, String> serialNumById;

    public TestPlatformSerialNumStorageService() {
        this.serialNumById = Caffeine.newBuilder()
                .maximumSize(1000)
                .build();
    }

    public void saveSerialNum(String ulTestCaseId, String serialNum) {
        serialNumById.put(ulTestCaseId, serialNum);
    }

    public String getSerialNum(String ulTestCaseId) {
        return serialNumById.getIfPresent(ulTestCaseId);
    }
}
