package com.danijel.bank_app_leapwise.util;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvGenerator {

    public static <T> String generateCsv(T item) {
        return getItemValues(item, 1);
    }

    //Skip number due to ID always being null, so csv would always start with comma.
    private static <T> String getItemValues(T item, int skipNumber) {
        Field[] fields = item.getClass().getDeclaredFields();
        return Stream.of(fields)
                .skip(skipNumber)
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(item) == null ? "" : field.get(item).toString();
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(","));
    }
}