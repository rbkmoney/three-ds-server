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
    private final String tag;

    public static DirectoryServerProvider of(String tag) {
        return stream(DirectoryServerProvider.values())
                .filter(p -> p.getTag().equals(tag))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown tag: " + tag));
    }
}
