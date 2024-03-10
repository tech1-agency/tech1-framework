package io.tech1.framework.domain.asserts;

import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.domain.reflections.ReflectionProperty;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.time.ZoneId.getAvailableZoneIds;
import static java.util.Objects.isNull;

@UtilityClass
public class Asserts {

    // =================================================================================================================
    // 1 assert complexity
    // =================================================================================================================
    public static void assertNonNullOrThrow(Object object, String message) {
        if (isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNonNullPropertyOrThrow(ReflectionProperty reflectionProperty) {
        if (isNull(reflectionProperty)) {
            throw new IllegalArgumentException("Unknown reflection property");
        }
        assertNonNullOrThrow(reflectionProperty.getPropertyValue(), invalidAttribute(reflectionProperty.getTreePropertyName()));
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

    public static <T> T requireNonNullOrThrow(T object, String message) {
        if (isNull(object)) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static <T> void assertUniqueOrThrow(Collection<T> options, T object, PropertyId propertyId) {
        if (options.contains(object)) {
            throw new IllegalArgumentException(
                    "Property %s must be unique".formatted(propertyId.value())
            );
        }
    }

    // =================================================================================================================
    // 1+ asserts complexity
    // =================================================================================================================
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
        if (!getAvailableZoneIds().contains(zoneId)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertDateTimePatternOrThrow(String dateTimePattern, String message) {
        assertNonNullNotBlankOrThrow(dateTimePattern, message);
        assertNonNullOrThrow(DateTimeFormatter.ofPattern(dateTimePattern), message);
    }
}
