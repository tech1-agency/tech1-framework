package io.tech1.framework.domain.utilities.collections;

import lombok.experimental.UtilityClass;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.CollectionUtils.firstElement;
import static org.springframework.util.CollectionUtils.lastElement;

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

    public static <T> T safeFirstElement(List<T> list) {
        return requireNonNull(firstElement(list));
    }

    public static <T> T safeFirstElement(Set<T> set) {
        return requireNonNull(firstElement(set));
    }

    public static <T> T safeLastElement(List<T> list) {
        return requireNonNull(lastElement(list));
    }

    public static <T> T safeLastElement(Set<T> set) {
        return requireNonNull(lastElement(set));
    }
}
