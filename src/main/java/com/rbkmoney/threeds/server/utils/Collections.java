package com.rbkmoney.threeds.server.utils;

import java.util.ArrayList;
import java.util.List;

public class Collections {

    public static <T> List<T> safeList(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }
}
