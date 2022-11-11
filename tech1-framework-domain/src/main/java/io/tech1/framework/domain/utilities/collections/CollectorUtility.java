package io.tech1.framework.domain.utilities.collections;

import lombok.experimental.UtilityClass;

import java.util.stream.Collector;

@UtilityClass
public class CollectorUtility {

    public static <T> Collector<T, ?, T> toSingleton() {
        return java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException("Collector toSingleton() expects one element");
                    }
                    return list.get(0);
                }
        );
    }
}
