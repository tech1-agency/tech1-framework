package io.tech1.framework.domain.utilities.enums;

import io.tech1.framework.domain.enums.EnumValue;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.function.Predicate;

import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttributeRequiredMissingValues;

@UtilityClass
public class EnumCreatorUtility {
    public static <E extends Enum<E> & EnumValue<String>> E findEnumByValueIgnoreCaseOrThrow(Class<E> enumClass, String value) {
        Predicate<E> filter = e -> e.getValue().equalsIgnoreCase(value);
        return findEnumByPredicateIgnoreCaseOrThrow(enumClass, value, filter);
    }

    public static <E extends Enum<E> & EnumValue<String>> E findEnumByNameOrThrow(Class<E> enumClass, String name) {
        Predicate<E> filter = e -> e.name().equals(name);
        return findEnumByPredicateIgnoreCaseOrThrow(enumClass, name, filter);
    }

    public static <E extends Enum<E>> E findUnknownValue(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> "UNKNOWN".equals(e.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(enumClass.getSimpleName() + " does not have UNKNOWN enum value"));
    }

    // ================================================================================================================
    // PRIVATE METHODS
    // ================================================================================================================
    public static <E extends Enum<E> & EnumValue<String>> E findEnumByPredicateIgnoreCaseOrThrow(Class<E> enumClass, String param, Predicate<E> predicate) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> {
                    var message = invalidAttributeRequiredMissingValues(
                            enumClass.getSimpleName(),
                            baseJoining(enumClass),
                            param
                    );
                    return new IllegalArgumentException(message);
                });
    }
}
