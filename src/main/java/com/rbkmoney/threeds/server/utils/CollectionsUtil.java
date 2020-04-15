package com.rbkmoney.threeds.server.utils;

import java.util.Collections;
import java.util.List;

public class CollectionsUtil {

    public static <T> List<T> safeCollectionList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
