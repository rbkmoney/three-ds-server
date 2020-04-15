package com.rbkmoney.threeds.server.serialization;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListWrapper<T> extends Garbage {

    private List<T> value;

    @Override
    public String toString() {
        return value == null
                ? super.toString()
                : collect();
    }

    private String collect() {
        return value.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

}
