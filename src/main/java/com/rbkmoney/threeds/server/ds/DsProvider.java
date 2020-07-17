package com.rbkmoney.threeds.server.ds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public enum DsProvider {

    VISA("visa"),
    MASTERCARD("mastercard"),
    MIR("mir");

    @Getter
    private final String id;

    public static DsProvider of(String providerId) {
        return stream(DsProvider.values())
                .filter(p -> p.getId().equals(providerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown providerId: " + providerId));
    }
}
