package io.tech1.framework.domain.utilities.collections;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class CollectionUtility {

    public static <T> Queue<T> emptyQueue() {
        return new LinkedList<>();
    }

    public static <T> List<T> union(List<T> one, List<T> second) {
        List<T> list = new ArrayList<>(one.size() + second.size());
        list.addAll(one);
        list.addAll(second);
        return list;
    }

    public static <T> Set<T> union(Set<T> one, Set<T> second) {
        Set<T> list = new HashSet<>(one.size() + second.size());
        list.addAll(one);
        list.addAll(second);
        return list;
    }
}
