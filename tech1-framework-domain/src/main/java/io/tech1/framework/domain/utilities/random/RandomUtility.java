package io.tech1.framework.domain.utilities.random;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.security.SecureRandom;

import static java.lang.Math.abs;

@UtilityClass
public class RandomUtility {

    private static final SecureRandom RND = new SecureRandom();

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

    public static Short randomShort() {
        return (short) RND.nextInt(Short.MAX_VALUE);
    }

    public static boolean randomBoolean() {
        return RND.nextBoolean();
    }

    public static Double randomDouble() {
        return RND.nextDouble();
    }

    public static Integer randomInteger() {
        return RND.nextInt();
    }

    public static Integer randomIntegerGreaterThanZero() {
        return abs(randomInteger());
    }

    public static Integer randomIntegerLessThanZero() {
        return -randomIntegerGreaterThanZero();
    }

    public static Integer randomIntegerGreaterThanZeroByBounds(int lowerBound, int upperBound) {
        return lowerBound + 1 + RND.nextInt(upperBound - lowerBound - 2);
    }

    public static Long randomLong() {
        return RND.nextLong();
    }

    public static Long randomLongGreaterThanZero() {
        return abs(RND.nextLong());
    }

    public static Long randomLongLessThanZero() {
        return -abs(RND.nextLong());
    }

    public static Long randomLongGreaterThanZeroByBounds(long lowerBound, long upperBound) {
        return lowerBound + 1 + RND.nextInt((int) (upperBound - lowerBound - 2));
    }
}
