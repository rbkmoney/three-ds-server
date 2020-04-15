package com.rbkmoney.threeds.server.serialization;

import lombok.Data;

@Data
public class Garbage {

    private Object garbageValue;

    public boolean isGarbage() {
        return garbageValue != null;
    }
}
