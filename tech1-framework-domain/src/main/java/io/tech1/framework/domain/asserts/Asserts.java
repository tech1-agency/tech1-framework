package io.tech1.framework.domain.asserts;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class Asserts {

    public static void assertNonNullOrThrow(Object object, String message) {
        if (isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNonBlankOrThrow(String object, String message) {
        if (object.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNonEmptyOrThrow(Collection<?> collection, String message) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrueOrThrow(boolean flag, String message) {
        if (!flag) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertFalseOrThrow(boolean flag, String message) {
        if (flag) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNonNullNotBlankOrThrow(Object object, String message) {
        assertNonNullOrThrow(object, message);
        assertNonBlankOrThrow(object.toString(), message);
    }

    public static void assertNonNullNotEmptyOrThrow(Collection<?> collection, String message) {
        assertNonNullOrThrow(collection, message);
        assertNonEmptyOrThrow(collection, message);
    }

    public static void assertZoneIdOrThrow(String zoneId, String message) {
        assertNonNullNotBlankOrThrow(zoneId, message);
        if (!ZoneId.getAvailableZoneIds().contains(zoneId)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertDateTimePatternOrThrow(String dateTimePattern, String message) {
        assertNonNullNotBlankOrThrow(dateTimePattern, message);
        assertNonNullOrThrow(DateTimeFormatter.ofPattern(dateTimePattern), message);
    }

    public static <T> T requireNonNullOrThrow(T object, String message) {
        if (isNull(object)) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }
}
