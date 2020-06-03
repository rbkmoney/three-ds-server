package com.rbkmoney.threeds.server.utils;

import com.rbkmoney.threeds.server.domain.message.MessageExtension;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class MessageExtensionUtil {

    private static final int MAX_SIZE = 10;
    private static final int MAX_LENGTH_DATA = 8059;
    private static final int MAX_LENGTH_NAME = 64;
    private static final int MAX_LENGTH_ID = 64;

    public static String getIdCriticalMessage(List<MessageExtension> messageExtension) {
        return messageExtension.stream()
                .filter(Objects::nonNull)
                .filter(MessageExtension::getCriticalityIndicator)
                .map(MessageExtension::getId)
                .collect(Collectors.joining(", "));
    }

    public static boolean isInvalidSize(List<MessageExtension> messageExtension) {
        return messageExtension.size() > MAX_SIZE;
    }

    public static boolean isCriticalMessage(List<MessageExtension> messageExtension) {
        return messageExtension.stream()
                .filter(Objects::nonNull)
                .anyMatch(MessageExtension::getCriticalityIndicator);
    }

    public static boolean isInvalidData(List<MessageExtension> messageExtension) {
        return messageExtension.stream()
                .flatMap(me -> me.getData().entrySet().stream())
                .map(Map.Entry::getValue)
                .anyMatch(o -> o == null || o.toString().length() > MAX_LENGTH_DATA);
    }

    public static boolean isInvalidName(List<MessageExtension> messageExtension) {
        return messageExtension.stream()
                .map(MessageExtension::getName)
                .anyMatch(s -> isBlank(s) || s.length() > MAX_LENGTH_NAME);
    }

    public static boolean isInvalidId(List<MessageExtension> messageExtension) {
        return messageExtension.stream()
                .map(MessageExtension::getId)
                .anyMatch(s -> isBlank(s) || s.length() > MAX_LENGTH_ID);
    }
}
