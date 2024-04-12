package io.tech1.framework.domain.asserts;

import io.tech1.framework.domain.base.PropertyId;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static io.tech1.framework.domain.utilities.numbers.BigDecimalUtility.*;
import static java.time.ZoneId.getAvailableZoneIds;
import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;

@UtilityClass
public class Asserts {

    // =================================================================================================================
    // 1 assert complexity: non null
    // =================================================================================================================
    public static void assertNonNullOrThrow(Object object, String message) {
        if (isNull(object)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNonNullOrThrow(Object object) {
        assertNonNullOrThrow(object, null);
    }

    // =================================================================================================================
    // 1 assert complexity: non blank
    // =================================================================================================================
    public static void assertNonBlankOrThrow(String object, String message) {
        if (object.isBlank()) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNonBlankOrThrow(String object) {
        assertNonBlankOrThrow(object, null);
    }

    // =================================================================================================================
    // 1 assert complexity: has length
    // =================================================================================================================
    public static void assertHasLengthOrThrow(String object, String message) {
        if (!hasLength(object)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertHasLengthOrThrow(String object) {
        assertHasLengthOrThrow(object, null);
    }

    // =================================================================================================================
    // 1 assert complexity: true/false
    // =================================================================================================================
    public static void assertTrueOrThrow(boolean condition, String message) {
        if (!condition) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertTrueOrThrow(boolean condition) {
        assertTrueOrThrow(condition, null);
    }

    public static void assertFalseOrThrow(boolean condition, String message) {
        if (condition) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertFalseOrThrow(boolean condition) {
        assertFalseOrThrow(condition, null);
    }

    // =================================================================================================================
    // 1 assert complexity: long
    // =================================================================================================================
    public static void assertPositiveOrThrow(long value, String message) {
        if (value > 0) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertPositiveOrThrow(long value) {
        assertPositiveOrThrow(value, null);
    }

    public static void assertPositiveOrZeroOrThrow(long value, String message) {
        if (value >= 0) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertPositiveOrZeroOrThrow(long value) {
        assertPositiveOrZeroOrThrow(value, null);
    }

    public static void assertNegativeOrThrow(long value, String message) {
        if (value < 0) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNegativeOrThrow(long value) {
        assertNegativeOrThrow(value, null);
    }

    public static void assertNegativeOrZeroOrThrow(long value, String message) {
        if (value <= 0) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNegativeOrZeroOrThrow(long value) {
        assertNegativeOrZeroOrThrow(value, null);
    }

    // =================================================================================================================
    // 1 assert complexity: BigDecimal
    // =================================================================================================================

    public static void assertPositiveOrThrow(BigDecimal value, String message) {
        if (isPositive(value)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertPositiveOrThrow(BigDecimal value) {
        assertPositiveOrThrow(value, null);
    }

    public static void assertPositiveOrZeroOrThrow(BigDecimal value, String message) {
        if (isPositiveOrZero(value)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertPositiveOrZeroOrThrow(BigDecimal value) {
        assertPositiveOrZeroOrThrow(value, null);
    }

    public static void assertNegativeOrThrow(BigDecimal value, String message) {
        if (isNegative(value)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNegativeOrThrow(BigDecimal value) {
        assertNegativeOrThrow(value, null);
    }

    public static void assertNegativeOrZeroOrThrow(BigDecimal value, String message) {
        if (isNegativeOrZero(value)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNegativeOrZeroOrThrow(BigDecimal value) {
        assertNegativeOrZeroOrThrow(value, null);
    }

    // =================================================================================================================
    // 1 assert complexity: collections
    // =================================================================================================================
    public static void assertNonEmptyOrThrow(Collection<?> collection, String message) {
        if (isEmpty(collection)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void assertNonEmptyOrThrow(Collection<?> collection) {
        assertNonEmptyOrThrow(collection, null);
    }

    public static <T> void assertUniqueOrThrow(Collection<T> options, T object, PropertyId propertyId) {
        if (options.contains(object)) {
            throw new IllegalArgumentException(
                    "Property %s must be unique".formatted(propertyId.value())
            );
        }
    }
    // =================================================================================================================
    // 1 assert complexity: require
    // =================================================================================================================
    public static <T> T requireNonNullOrThrow(T object, String message) {
        if (isNull(object)) {
            if (hasLength(message)) {
                throw new IllegalArgumentException(message);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return object;
    }

    public static <T> T requireNonNullOrThrow(T object) {
        return requireNonNullOrThrow(object, null);
    }

    // =================================================================================================================
    // 1+ asserts complexity
    // =================================================================================================================
    public static void assertZoneIdOrThrow(String zoneId, String message) {
        assertHasLengthOrThrow(zoneId, message);
        if (!getAvailableZoneIds().contains(zoneId)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertDateTimePatternOrThrow(String dateTimePattern, String message) {
        assertHasLengthOrThrow(dateTimePattern, message);
        assertNonNullOrThrow(DateTimeFormatter.ofPattern(dateTimePattern), message);
    }
}
