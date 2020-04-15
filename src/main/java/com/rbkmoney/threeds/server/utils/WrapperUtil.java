package com.rbkmoney.threeds.server.utils;

import com.rbkmoney.threeds.server.domain.Valuable;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

public class WrapperUtil {

    public static <T> List<T> getListWrapperValue(ListWrapper<T> listWrapper) {
        return Optional.ofNullable(listWrapper).map(ListWrapper::getValue).orElse(null);
    }

    public static <T extends Valuable> T getEnumWrapperValue(EnumWrapper<T> enumWrapper) {
        return Optional.ofNullable(enumWrapper).map(EnumWrapper::getValue).orElse(null);
    }

    public static Object getEnumWrapperGarbageValue(EnumWrapper enumWrapper) {
        return Optional.ofNullable(enumWrapper).map(EnumWrapper::getGarbageValue).orElse(null);
    }

    public static <T extends TemporalAccessor> T getTemporalAccessorValue(TemporalAccessorWrapper<T> temporalAccessorWrapper) {
        return Optional.ofNullable(temporalAccessorWrapper).map(TemporalAccessorWrapper::getValue).orElse(null);
    }

    public static Object getTemporalAccessorGarbageValue(TemporalAccessorWrapper temporalAccessorWrapper) {
        return Optional.ofNullable(temporalAccessorWrapper).map(TemporalAccessorWrapper::getGarbageValue).orElse(null);
    }

    public static <T extends Valuable> ConstraintValidationResult validateRequiredConditionField(EnumWrapper<T> enumWrapper, String fieldName) {
        if (enumWrapper == null) {
            return ConstraintValidationResult.failure(NOT_NULL, fieldName);
        }

        Object garbageValue = getEnumWrapperGarbageValue(enumWrapper);
        if (garbageValue != null) {
            if (garbageValue.toString().equals("")) {
                return ConstraintValidationResult.failure(NOT_NULL, fieldName);
            }

            return ConstraintValidationResult.failure(PATTERN, fieldName);
        }

        return ConstraintValidationResult.success();
    }

    public static <T extends TemporalAccessor> ConstraintValidationResult validateRequiredConditionField(TemporalAccessorWrapper<T> enumWrapper, String fieldName) {
        if (enumWrapper == null) {
            return ConstraintValidationResult.failure(NOT_NULL, fieldName);
        }

        Object garbageValue = getTemporalAccessorGarbageValue(enumWrapper);
        if (garbageValue != null) {
            if (garbageValue.toString().equals("")) {
                return ConstraintValidationResult.failure(NOT_NULL, fieldName);
            }

            return ConstraintValidationResult.failure(PATTERN, fieldName);
        }

        return ConstraintValidationResult.success();
    }
}
