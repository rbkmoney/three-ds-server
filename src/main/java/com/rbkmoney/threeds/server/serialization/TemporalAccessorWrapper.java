package com.rbkmoney.threeds.server.serialization;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.temporal.TemporalAccessor;

@EqualsAndHashCode(callSuper = true)
@Data
public class TemporalAccessorWrapper<T extends TemporalAccessor> extends Garbage {

    private T value;

    @Override
    public String toString() {
        return value == null ? super.toString() : value.toString();
    }

}
