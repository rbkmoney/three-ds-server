package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPlatformSerialNumStorageServiceTest {

    private static final String TRAP = "TRAP";

    private TestPlatformSerialNumStorageService testPlatformSerialNumStorageService =
            new TestPlatformSerialNumStorageService();

    @Test
    public void shouldSaveAndGetTransactionInfo() {
        String id = UUID.randomUUID().toString();
        String expected = "serialNum";

        testPlatformSerialNumStorageService.saveSerialNum(TRAP, TRAP);
        testPlatformSerialNumStorageService.saveSerialNum(id, expected);

        String actual = testPlatformSerialNumStorageService.getSerialNum(id);
        assertThat(actual).isEqualTo(expected);
    }
}
