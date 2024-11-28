package ru.practicum.util;

import java.util.Collections;
import java.util.List;

public class Util {
    public static <T> List<T> applyPagination(List<T> list, int from, int size) {
        if (from >= list.size()) return Collections.emptyList();
        return list.subList(from, Math.min(from + size, list.size()));
    }
}
