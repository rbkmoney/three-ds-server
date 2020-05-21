package com.rbkmoney.threeds.server.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public enum DirectoryServerProvider {

    VISA("visa"),
    MASTERCARD("mastercard"),
    MIR("mir"),
    TEST("test");

    @Getter
    private final String id;

    public static DirectoryServerProvider of(String providerId) {
        return stream(DirectoryServerProvider.values())
                .filter(p -> p.getId().equals(providerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown providerId: " + providerId));
    }
}
