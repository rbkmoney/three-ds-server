package com.rbkmoney.threeds.server.serialization;

import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnumWrapper<T extends Valuable> extends Garbage {

    private T value;

    @Override
    public String toString() {
        return value == null ? super.toString() : value.toString();
    }
}
