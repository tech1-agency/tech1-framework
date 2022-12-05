package io.tech1.framework.domain.utilities.enums;

import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@UtilityClass
public class EnumUtility {

    public static <E extends Enum<E>> Set<String> getEnumNames(Class<E> enumClass) {
        assertNonNullOrThrow(enumClass, invalidAttribute("enumClass"));
        return EnumSet.allOf(enumClass).stream().map(Enum::name).collect(Collectors.toSet());
    }

    public static <E extends Enum<E>> Set<E> set(Class<E> enumClass) {
        return Stream.of(enumClass.getEnumConstants()).collect(Collectors.toSet());
    }

    public static <E extends Enum<E>> String baseJoining(Class<E> enumClass) {
        return Stream.of(enumClass.getEnumConstants()).map(Enum::toString).sorted().collect(Collectors.joining(", "));
    }

    public static <E extends Enum<E>> String baseJoining(Set<E> enums) {
        return enums.stream().map(Enum::toString).sorted().collect(Collectors.joining(", "));
    }
}
