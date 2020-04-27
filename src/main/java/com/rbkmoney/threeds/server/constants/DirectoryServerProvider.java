package com.rbkmoney.threeds.server.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DirectoryServerProvider {

    VISA("visa"),
    MASTERCARD("mastercard"),
    MIR("mir"),
    TEST("test");

    @Getter
    private final String tag;
}
