package com.rbkmoney.threeds.server.domain.root;

import org.springframework.lang.Nullable;

public interface TransactionalMessage {

    @Nullable
    default String getAcsTransID() {
        return null;
    }

    @Nullable
    default String getDsTransID() {
        return null;
    }

    @Nullable
    default String getThreeDSServerTransID() {
        return null;
    }

    @Nullable
    default String getSdkTransID() {
        return null;
    }
}
