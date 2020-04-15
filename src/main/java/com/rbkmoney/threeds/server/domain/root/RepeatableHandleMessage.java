package com.rbkmoney.threeds.server.domain.root;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface RepeatableHandleMessage {

    default void setHandleRepetitionNeeded(boolean handleRepetitionNeeded) {
    }

    @JsonIgnore
    default boolean isHandleRepetitionNeeded() {
        return false;
    }
}
