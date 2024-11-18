package jbst.foundation.domain.comparators;

import jbst.foundation.domain.reflections.ReflectionProperty;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Comparator;

@UtilityClass
public class ReflectionsComparators {
    public static final Comparator<Field> PROPERTIES_ASSERTION_COMPARATOR = (o1, o2) -> {
        if ("enabled".equals(o1.getName())) {
            return -1;
        } else if ("enabled".equals(o2.getName())) {
            return 1;
        }
        return o1.getName().compareTo(o2.getName());
    };

    public static final Comparator<ReflectionProperty> PROPERTIES_PRINTER_COMPARATOR = (o1, o2) -> {
        if ("enabled".equals(o1.getPropertyName())) {
            return -1;
        } else if ("enabled".equals(o2.getPropertyName())) {
            return 1;
        }
        return o1.getReadableValue().compareTo(o2.getReadableValue());
    };
}
