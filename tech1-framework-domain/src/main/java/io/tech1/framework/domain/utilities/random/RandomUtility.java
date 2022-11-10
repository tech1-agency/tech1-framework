package io.tech1.framework.domain.utilities.random;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class RandomUtility {

    public static Number one(Class<? extends Number> clazz) {
        if (clazz == Short.class) {
            return 1;
        }
        if (clazz == Integer.class) {
            return 1;
        }
        if (clazz == Long.class) {
            return 1L;
        }
        if (clazz == Double.class) {
            return 1.0d;
        }
        if (clazz == BigDecimal.class) {
            return BigDecimal.ONE;
        }
        throw new IllegalArgumentException("Unexpected clazz: " + clazz.getName());
    }
}
